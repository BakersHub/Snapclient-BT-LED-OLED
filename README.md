# Snapclient-BT-LED-OLED
Snapclient & Bluetooth receiver with inbuilt LED ARGB sound reactive controller

Screenshots are coming

## Flashing the ESP32 Firmware

For most users, the easiest way to flash is to use the prebuilt merged image `SnapClient-BT-LED-OLEDv1.x.bin` with ESPHome Web Flasher or ESPHome Flasher.

**Quick path:**
- Board: ESP32‑WROVER (or compatible devkit).
- Firmware: `SnapClient-BT-LED-OLEDv1.x.bin` from this repository.
- Tool: ESPHome Web Flasher / ESPHome Flasher.

Then follow the step‑by‑step guide in [FLASHING_QUICKSTART.md](FLASHING_QUICKSTART.md) for:
- Detailed flashing steps.
- First‑boot Wi‑Fi AP behavior.
- Web UI / captive portal access at `ESP32-SNAPCLIENT-******` / `http://192.168.4.1`.
- Minimum Wi‑Fi and Snapserver settings to get audio playing.

## Source Code Layout

- ESP32 firmware project (upstream Snapclient, modified by BakersHub) lives in the submodule:
	- `ESP32WROVER/snap-bt-led-oled` → points to https://github.com/BakersHub/snapclient (branch `Snapclient-BT-LED-OLED`).
- Prebuilt ESP32 firmware binary for this project: `SnapClient-BT-LED-OLEDv1.1.bin` in this repository.

All GPL-3.0–licensed source used to build the distributed firmware is available either directly in this repository or in the linked submodule fork above.

## License

This project is licensed under the GNU General Public License v3.0 (GPL-3.0).

This repository contains modified versions of Snapcast components (https://github.com/CarlosDerSeher/snapclient).  
Original authors’ work is preserved, and modifications were made by BakersHub.

Source code for all distributed binaries (Android APK and ESP32 firmware) is included in this repository and its submodule.

Snapclient (BakersHub) Copyright (C) 2025 BakersHub

This program comes with ABSOLUTELY NO WARRANTY; for details see LICENSE.  
This program is free software: you can redistribute it and/or modify it under GPL-3.0.

SPECIAL MENTIONS & THANKS to
@Aximut
Source Code includes the usability of BLUETOOTH thanks to the work of 
@Aximut 

BLUETOOTH is mentioned to have some improvements to be made, I'm sure to advise end users of potential issues highlighted by code creator, although edits further edits have been made by @BakersHub (myself) to eliminate such issues, as of time of writing, no issues have arisen since my edits have been made. but with 1 person debugging it's not ruled out of any issues been eliminated. please use and report any issues with detail for increased chance of fixes been made ASAP.