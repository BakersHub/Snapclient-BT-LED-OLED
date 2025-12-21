# Snapclient BT + LED + SH1106 Overview

This fork of `snapclient` turns an ESP32 board into a self‑contained Snapcast client with:

- Snapcast network audio playback (as in the original project).
- Optional Bluetooth A2DP input with on‑device transport/volume control.
- A SH1106/SSD1306 128×64 OLED status display.
- A sound‑reactive addressable LED controller (WS2812/SK6812).
- Physical **volume up/down** and **LED on/off** buttons (GPIO‑configurable).
- Automatic **Wi‑Fi AP recovery / reset** path when the configured network is unreachable (after ~5 failed connect attempts, roughly 5–10 seconds).
- A built‑in web UI for configuration and live control.
- NVS‑backed persistent configuration for Wi‑Fi, Snapserver, display, LEDs, and more.
- GPIO mappings for these features are configurable, with the usual ESP32 caution to avoid boot/strapping pins.

Everything runs on a single ESP32 (or ESP32‑S2) without needing an external host.

---

## High‑Level Architecture

- **Core player**: Snapcast client (`lightsnapcast`) pulls PCM audio from a Snapserver.
- **Bluetooth input**: A2DP sink provides an alternate input path, feeding the same audio pipeline.
- **Display**: SH1106/SSD1306 driver draws connection state, metadata, volume, Wi‑Fi, time, and (optionally) an EQ view.
- **LED controller**: Custom component drives an addressable LED strip and reacts to the current audio stream.
- **Config store (NVS)**: `system_config` and `led_config` structs are saved/loaded from NVS at boot and via the web UI.
- **Web UI + HTTP server**: A small HTTP server exposes JSON+form endpoints; a single‑page HTML app talks to them via `fetch()`.

Most of the glue lives in [main/main.c](../main/main.c) and the `components/` directory.

---

## Major Features This Fork Adds

### 1. Bluetooth A2DP + AVRCP

- ESP32 acts as a **Bluetooth A2DP sink**:
  - Any phone/PC can connect and stream audio to the device.
  - Playback is piped through the same output/DSP path that Snapcast uses.
- **AVRCP** integration:
  - Remote control for Play/Pause/Next/Previous (depending on your board’s buttons).
  - Bluetooth volume changes are tracked and reflected on the device.
- The *Bluetooth source device name* is displayed on the OLED in BT mode.

The Bluetooth implementation lives under [components/bluetooth](../components/bluetooth).

---

### 2. SH1106 / SSD1306 OLED Display

- 128×64 I²C OLED, supported for:
  - SH1106‑based 1.28" panels (often 132×64 internally, visible 128×64).
  - SSD1306‑style 0.96" 128×64 panels.
- Display driver and UI are implemented in [components/display_sh1106](../components/display_sh1106).

**Modes and layout**

- **Snapclient mode** (when playing from Snapserver):
  - Shows connection status, Snapserver info, volume, Wi‑Fi strength, and time.
  - Focuses on clear status instead of EQ graphics (to keep I²C load low).
- **Bluetooth mode** (when BT is active):
  - Top row: scrolling Bluetooth device/source name (left) and clock (right).
  - Middle/bottom rows: connection/volume status and a simple EQ‑style visualization.

**Column offset (panel compatibility)**

- Different modules wire the SH1106/SSD1306 origin differently. This fork:
  - Adds a **runtime‑settable column offset** inside the display driver.
  - Exposes it via `system_config` and the web UI as a "Display Size" choice.

Typical values:

- "1.28" SH1106" → column offset `2`.
- "0.96" 128×64" (SSD1306‑style) → column offset `0`.

At boot `main` loads `system_config`, calls `display_set_column_offset()`, and the driver applies the correct offset when issuing SH1106 column‑set commands.

---

### 3. Sound‑Reactive LED Controller

- Drives a WS2812 / SK6812 (or similar) LED strip via RMT.
- Consumes audio samples from the Snapcast (or BT) PCM stream and turns them into:
  - Spectrum‑style level bars.
  - VU‑meter effects.
  - Various color/animation patterns.
- Configuration is persisted in NVS and can be changed live via the web UI.

The core logic lives in [components/lightsnapcast](../components/lightsnapcast) and the LED‑specific pieces in a dedicated LED controller component (see `components/` directory).

Key behaviors:

- **Audio feed**: The player calls a feed function whenever new PCM data is available; the LED code performs simple analysis (levels, optional bass focus).
- **Brightness and effect selection**: Exposed as config fields in NVS and reflected in the web UI.
- **Playback volume vs raw audio**:
  - You can choose whether LEDs follow the **post‑volume signal** or the **raw audio** (ignore volume) for more consistent visuals.

---

### 4. NVS‑Backed Configuration

Two main configuration structs are persisted to NVS:

- **System config** (`system_config`):
  - Wi‑Fi credentials.
  - Snapserver host/port and Snapclient name.
  - Snapcast gain boost and related audio settings.
  - Display settings (including `sh1106_column_offset`).
  - Optional device‑specific fields (e.g., board variant, ETH/Wi‑Fi selection).
- **LED config** (`led_config`):
  - LED strip length and pin mapping.
  - Default effect, brightness, and color settings.
  - Options like "Ignore Playback Volume (Auto Gain)" and "Default Bass Focus".

Configuration is:

- Loaded at boot.
- Updated from the web UI and immediately applied.
- Saved back to NVS on changes, so settings survive power cycles.

Implementation details are in the `components/system_config` and LED‑related components.

---

### 5. Built‑In Web UI

The web UI is a single‑page app served from the ESP32 flash (SPIFFS/littlefs) with accompanying JS/CSS in [html/](../html) and [components/ui_http_server/html/index.html](../components/ui_http_server/html/index.html).

**Key sections in the UI:**

- **System & Device Configuration**
  - Wi‑Fi and Snapserver settings.
  - Snapclient name (also used as the web UI heading and display label).
  - Display Size selector (sets the SH1106/SSD1306 column offset).
  - Helpful inline notes for display options.

- **Audio / DSP / Gain**
  - Snapcast Gain Boost, with an inline **help panel** describing:
    - What gain values like 0.5×, 1×, 2× mean.
    - Perceived loudness vs clipping risk.
    - Recommended defaults.

- **LED Configuration**
  - Effect selection, brightness, and color controls.
  - Toggle for "Ignore Playback Volume (Auto Gain)" with a short explanation.
  - "Default Bass Focus" slider or toggle, with helper text on how it affects visuals.

The UI talks to these HTTP endpoints (implemented in [components/ui_http_server](../components/ui_http_server)):

- `/system/config` – GET (JSON) and POST (form) for system config.
- `/led/config` – GET/POST for LED settings.
- `/dsp/config` – GET/POST for DSP/EQ parameters.

The POST handlers were updated to read the **entire request body** based on `content_len`, fixing earlier issues where long forms (with many fields) could be truncated.

---

### 6. Logging and Performance Tweaks

- Snapcast player’s PCM allocator sometimes has to insert a **silent chunk** if memory is briefly tight (e.g., while Bluetooth is doing work).
- Previously, this emitted WARN‑level logs like "couldn't get memory to insert chunk" with heap dumps, which:
  - Looked alarming even though the behavior is expected and non‑fatal.
  - Added log overhead on volume changes.
- In this fork those logs are **downgraded to debug level** and clarified with comments.

You can still enable verbose logging when debugging, but in normal builds the device remains quieter and a bit more efficient.

---

### 7. Physical Controls & GPIO Notes

- **Hardware volume buttons**
  - Dedicated GPIOs can be assigned for **Volume Up/Down**.
  - Button presses adjust the Snapcast / BT playback volume and are reflected on the OLED.
- **LED on/off button**
  - A separate GPIO can be used as a **LED enable/disable toggle**.
  - Useful for quickly turning off the light show while keeping audio playing.
- **AP / Wi‑Fi recovery button + behavior**
  - If Wi‑Fi credentials are invalid or the configured network is unreachable, the device can start its own **Wi‑Fi AP** so you can connect and fix settings through the web UI.
  - A dedicated GPIO button (or long‑press on an existing button, depending on board wiring) can also be configured to **reset Wi‑Fi / enter AP recovery mode**.
- **GPIO configurability and cautions**
  - The GPIOs used for buttons and LEDs are configurable (via Kconfig / board config), so you can adapt them to your hardware.
  - **Be careful when changing pins**: some ESP32 pins are **boot‑strapping / restricted pins** (for example GPIO0, GPIO2, GPIO12, GPIO15 on many modules) and **must not** be pulled to the wrong level at reset or used in ways that interfere with boot.
  - Always check the ESP32 datasheet / module pinout and avoid using boot pins or strapping pins for permanent buttons or heavy loads.

---

## Building and Flashing

This project uses ESP‑IDF. Typical workflows are already wired as VS Code tasks and described in [doc/docker_build.md](docker_build.md):

- **Native ESP‑IDF** (if you have the toolchain locally):
  - `ESP-IDF: Menuconfig (native)`
  - `ESP-IDF: Build (native)`
  - `ESP-IDF: Flash (native)`
  - `ESP-IDF: Monitor (native)`
- **Docker‑based build** (no local ESP‑IDF install required):
  - `Start Docker Container for Build`
  - `Build, Flash and Monitor (Docker)`

Choose your board variant and peripherals (Wi‑Fi vs ETH, codec, etc.) in `menuconfig` as documented in the original upstream project plus the additional options added by this fork (Bluetooth, display, LEDs).

---

## Upstream vs This Fork

Compared to the upstream `snapclient` ESP32 project, this fork mainly adds:

- Bluetooth A2DP sink and AVRCP integration.
- SH1106/SSD1306 OLED UI with support for multiple panel layouts via a column offset.
- A full sound‑reactive LED controller with persistent config.
- A richer web UI with inline help, live system/LED/DSP configuration, and a display size selector.
- Log‑level and robustness tweaks (e.g., HTTP body handling, less noisy allocator warnings).

If you are already familiar with the upstream project, skimming this document plus the web UI should be enough to get started with the BT+LED+display features.
