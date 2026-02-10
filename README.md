<p align="center">
  <img src="Images/Header.png" alt="Snapcast Header" width="80%">
</p>

# 🧪 Snapclient-BT-LED-OLED (BETA)

An **ESP32-based Snapcast client** with **Bluetooth audio input**, a **built-in sound-reactive ARGB LED controller**, and an **OLED status display** — designed for **multi-room, perfectly synced audio** in Home Assistant setups.

Built to be **easy to flash, easy to configure, and flexible to extend**.

> ⚠️ This project is currently **BETA**. Core functionality is stable, but features and documentation are still evolving.

---

## ✨ Key Features

- 🎶 **Snapcast network audio playback**
  - Tight sync across multiple speakers
  - Works great with Home Assistant / Music Assistant
- 📡 **Bluetooth A2DP receiver**
  - Stream directly from phone or PC
  - AVRCP transport & volume control
- 🌈 **Sound-reactive ARGB LED controller**
  - WS2812 / SK6812 compatible
  - **Only 3 wires required**
  - Dozens of effects that react and bounce to music
- 🖥️ **OLED display (SH1106 / SSD1306)**
  - Clock display
  - Playback / connection status
  - Bluetooth source name (in BT mode)
- 🌐 **Built-in Web UI**
  - Wi-Fi, Snapserver, audio, LED, and display config
  - No YAML, Arduino, or ESP-IDF required for normal use
- 🔁 **Automatic Wi-Fi AP recovery**
  - If Wi-Fi fails, the device exposes its own setup hotspot

---

## 🧭 System Overview

<img src="Install%20Guide%20Screenshots/FlowDiagram.png" alt="System Flow Diagram" width="90%">

This diagram shows how audio flows from Home Assistant / Snapcast into the ESP32, then out to the DAC, speakers, OLED, and sound-reactive LEDs.

<img src="Install%20Guide%20Screenshots/WiringDiagram.png" alt="System Flow Diagram" width="100%">

---

## 🧩 Supported Hardware

### ESP32 boards
- ESP32-WROVER (4MB)
- ESP32-S3 Dev (16MB)
- ESP32-S3 Zero (2MB)

### Audio DAC (required)
- **PCM5102A**
- **MAX98357A**

### Display (optional)
- SH1106 or SSD1306 128×64 I²C OLED

### LEDs (optional)
- WS2812 / SK6812 addressable RGB LEDs

📌 **GPIO mappings are documented** — see [`WROVER_GPIO_MAP.md`](WROVER_GPIO_MAP.md)

---

## 📸 Screenshots

<img src="Install%20Guide%20Screenshots/1.PNG" width="50%">

<details>
<summary>Click to expand</summary>


<img src="Install%20Guide%20Screenshots/2.PNG" width="50%">

<img src="Install%20Guide%20Screenshots/3.png" width="50%">

<img src="Install%20Guide%20Screenshots/4.PNG" width="50%">

<img src="Install%20Guide%20Screenshots/5.PNG" width="50%">

<img src="Install%20Guide%20Screenshots/6.PNG" width="50%">

<img src="Install%20Guide%20Screenshots/7.PNG" width="50%">

<img src="Install%20Guide%20Screenshots/8.png" width="50%">

<img src="Install%20Guide%20Screenshots/9.png" width="50%">

<img src="Install%20Guide%20Screenshots/10.png" width="50%">

<img src="Install%20Guide%20Screenshots/11.png" width="50%">

<img src="Install%20Guide%20Screenshots/12.png" width="50%">

<img src="Install%20Guide%20Screenshots/13.png" width="50%">

<img src="Install%20Guide%20Screenshots/14.png" width="50%">

<img src="Install%20Guide%20Screenshots/15.png" width="50%">

<img src="Install%20Guide%20Screenshots/16.png" width="50%">

</details>

---

## ⚡ Flashing & First-Time Setup

For most users, the easiest path is flashing the **prebuilt merged firmware image**:

- Firmware: `WROVER.bin`
- Tool: **ESPHome Web Flasher** or **ESPHome Flasher**
- Board: ESP32-WROVER (or compatible)

👉 Follow the step by step process in the screenshots above or the step-by-step guide here:  
**[`FLASHING_QUICKSTART.md`](FLASHING_QUICKSTART.md)**

It covers:
- Flashing the firmware
- First-boot Wi-Fi AP behavior
- Captive portal / web UI access (`http://192.168.4.1`)
- Minimum settings required to get audio playing

---

## 📁 Source Code Layout

- ESP32 firmware lives in the submodule:
  - `ESP32WROVER/snap-bt-led-oled`
  - Points to: https://github.com/BakersHub/snapclient  
    (branch: `Snapclient-BT-LED-OLED`)
- Prebuilt firmware binary:
  - `WROVER.bin`

All GPL-3.0–licensed source code used to build distributed binaries is available in this repository or its linked submodule.

---

## 🪪 License

This project is licensed under **GNU GPL v3.0**.

It contains modified Snapcast components from:  
https://github.com/CarlosDerSeher/snapclient

Original authors’ work is preserved.  
Modifications and additions by **BakersHub**.

This software is provided **without warranty**.  
See the `LICENSE` file for details.

---

## 🙌 Credits & Thanks

Special thanks to:

- **@Luar123**
- **@Aximut**
- **@badaix**
- **@bridadan**
- **@jorgenkraghjakobsen**
- **@CarlosDerSeher**

Bluetooth functionality is based on work by **@Aximut**, with further fixes and improvements by **BakersHub**.

Bluetooth is still an area of active development. While no major issues have been observed since recent fixes, testing and detailed issue reports are very welcome.
