# ESP32‑WROVER GPIO Map (Current Build)

This document describes how the ESP32‑WROVER module is wired **in this project’s default PCM5102A + SH1106 + LED + buttons build**.

Only **used** pins are listed. Unused GPIOs are left out so you can repurpose them, but keep in mind the ESP32’s boot/strapping pin rules.

---

## Power & Common Signals

These are standard WROVER dev‑kit pins, not configurable in software but important for wiring:

| WROVER pin | Signal | Connects to / Notes |
|-----------:|--------|---------------------|
| 5V         | 5 V    | Main 5 V input (from USB or external). Used to power the board and, via regulator, 3V3. Also typically feeds the external DAC/amp 5 V rail and LED strip 5 V. |
| 3V3        | 3.3 V  | 3.3 V rail. Powers ESP32 core and any 3V3 peripherals (e.g. SH1106 display, some logic on DAC board). Do **not** power 5 V devices directly from here. |
| GND        | GND    | Common ground for ESP32, DAC, amplifier, LED strip, buttons, and display. All external modules **must** share this ground. |

---

## Audio Path: I²S → PCM5102A DAC → Amplifier

These pins carry audio from the ESP32 to the PCM5102A DAC and control the power amp.

| GPIO | Direction | Function in this build | Connects to |
|-----:|-----------|------------------------|-------------|
| 0    | OUT       | `MCLK` (I²S master clock) | `SCK` / `MCLK` on PCM5102A board. Note: GPIO0 is a **boot strapping pin**; keep it pulled up at reset (the WROVER devkit already handles this). |
| 33   | OUT       | `BCK` (I²S bit clock) | `BCK` / `BCLK` on PCM5102A. |
| 32   | OUT       | `LRCK` / `WS` (I²S left/right clock) | `LRCK` / `L/RCLK` / `WS` on PCM5102A. |
| 25   | OUT       | `DATA` (I²S audio data out) | `DIN` / `SD` on PCM5102A. |
| 26   | OUT       | PCM5102A mute / XSMT | `XSMT` (mute / shutdown) on PCM5102A. Driven low on init, then toggled for mute control. |
| 12   | OUT       | Power amplifier enable (`PA_ENABLE_GPIO`) | Connect to amp board enable / shutdown pin (e.g. `EN` / `SHDN`). GPIO12 is a **strap pin**; ensure external circuitry keeps it at a safe level at boot (commonly pulled up via resistor and not driven low during reset). |

I²C control for the DAC is **not used** here:

- `CONFIG_DAC_I2C_SDA = -1`
- `CONFIG_DAC_I2C_SCL = -1`

The PCM5102A runs in hardware mode; only the mute/XSMT and the I²S bus are used.

---

## SH1106 / SSD1306 OLED Display (I²C)

The 128×64 OLED display is wired via I²C on the following pins:

| GPIO | Direction | Function | Connects to |
|-----:|-----------|----------|-------------|
| 21   | OUT/IN    | `SDA` (I²C data) | `SDA` on the SH1106/SSD1306 OLED module. |
| 22   | OUT       | `SCL` (I²C clock) | `SCL` / `SCK` on the OLED module. |

Display voltage:

- Most SH1106/SSD1306 128×64 modules in this project are **3.3 V‑friendly**. Connect their **VCC** to 3V3 and **GND** to common ground.

---

## LED Strip (WS2812 / SK6812)

A single GPIO drives the addressable LED strip via RMT:

| GPIO | Direction | Function | Connects to |
|-----:|-----------|----------|-------------|
| 23   | OUT       | LED strip data (`CONFIG_LED_GPIO_PIN`) | `DIN` on WS2812/SK6812 strip. |

Power for the strip:

- **5 V**: Connect the LED strip’s +5 V to the same 5 V rail used for the board.
- **GND**: Connect LED GND to **the same ground** as the ESP32.
- For longer strips, consider extra power injection and a level‑shifter from 3V3→5V (the default wiring assumes short strips that accept 3V3 data).

---

## Physical Buttons (Volume, LED Toggle, AP/Reset)

Volume buttons and the LED on/off button are software‑configurable, with these **defaults**:

| GPIO | Direction | Function (default) | Connects to |
|-----:|-----------|--------------------|-------------|
| 27   | IN        | Volume Up button (`VOLUME_UP_GPIO`) | One side of a momentary button; the other side to GND (with internal pull‑up enabled). |
| 14   | IN        | Volume Down button (`VOLUME_DOWN_GPIO`) | Same wiring style as Volume Up: button between pin and GND. |
| 18   | IN        | LED effect toggle / LED on‑off button (`LED_EFFECT_BUTTON_GPIO`) | Button between pin and GND. Toggles LED controller effect or turns LEDs off while leaving audio playing. |
| 19   | IN        | Wi‑Fi AP / recovery button (`AP_MODE_BUTTON_GPIO`) | Button between pin and GND (active high in code via pull‑up). Long‑press (~3 s) to force AP recovery / Wi‑Fi reset when normal Wi‑Fi is unreachable. Even without pressing this button, the firmware will automatically start its own AP after ~5 failed Wi‑Fi connection attempts (roughly 5–10 seconds) so you can fix credentials. |

Notes:

- Actual pin numbers can be changed at runtime via **system config / web UI** (stored in NVS).
- Wiring assumes **internal pull‑ups** and a button that pulls the GPIO **low** when pressed (or vice‑versa depending on config). Check the schematic and code comments if you invert the logic.

---

## UART / Debug

The standard serial port is used for programming and logs:

| GPIO | Direction | Function | Connects to |
|-----:|-----------|----------|-------------|
| 1    | OUT       | `U0TXD` (UART0 TX) | To USB‑to‑serial converter RX (on WROVER devkit this is already wired). |
| 3    | IN        | `U0RXD` (UART0 RX) | From USB‑to‑serial converter TX. |

Keep these free for flashing and monitoring. Don’t repurpose them for other peripherals in this build.

---

## Special / Strapping Pins and Cautions

Some of the GPIOs used in this design are also **ESP32 boot configuration pins**:

- **GPIO0** (used here as I²S `MCLK`).
- **GPIO12** (`PA_ENABLE_GPIO`).
- **GPIO5** (used internally in the LAN8720 strap logic if Ethernet is enabled).

Guidelines:

- Don’t hard‑pull these pins to the wrong level at reset (e.g. GPIO0 permanently low, GPIO12 pulled high with unusual voltages), or the ESP32 may fail to boot.
- Follow the WROVER devkit reference schematic and keep any external circuitry on these pins **high‑impedance during reset** (for example by using series resistors and avoiding strong pull‑downs).

For other free pins, always cross‑check with the **ESP32‑WROVER datasheet** to avoid ADC2/Wi‑Fi conflicts or strapping‑pin side‑effects.
