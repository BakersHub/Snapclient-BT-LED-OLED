# BETA Snapclient-BT-LED-OLED
Snapclient & Bluetooth receiver with inbuilt LED ARGB sound reactive controller

## Screenshots

1. <img src="Install%20Guide%20Screenshots/1.PNG" alt="Screenshot 1" width="50%">
2. <img src="Install%20Guide%20Screenshots/2.PNG" alt="Screenshot 2" width="50%">
3. <img src="Install%20Guide%20Screenshots/3.png" alt="Screenshot 3" width="50%">
4. <img src="Install%20Guide%20Screenshots/4.PNG" alt="Screenshot 4" width="50%">
5. <img src="Install%20Guide%20Screenshots/5.PNG" alt="Screenshot 5" width="50%">
6. <img src="Install%20Guide%20Screenshots/6.PNG" alt="Screenshot 6" width="50%">
7. <img src="Install%20Guide%20Screenshots/7.PNG" alt="Screenshot 7" width="50%">
8. <img src="Install%20Guide%20Screenshots/8.png" alt="Screenshot 8" width="50%">
9. <img src="Install%20Guide%20Screenshots/9.png" alt="Screenshot 9" width="50%">
10. <img src="Install%20Guide%20Screenshots/10.png" alt="Screenshot 10" width="50%">
11. <img src="Install%20Guide%20Screenshots/11.png" alt="Screenshot 11" width="50%">
12. <img src="Install%20Guide%20Screenshots/12.png" alt="Screenshot 12" width="50%">
13. <img src="Install%20Guide%20Screenshots/13.png" alt="Screenshot 13" width="50%">
14. <img src="Install%20Guide%20Screenshots/14.png" alt="Screenshot 14" width="50%">
15. <img src="Install%20Guide%20Screenshots/15.png" alt="Screenshot 15" width="50%">
16. <img src="Install%20Guide%20Screenshots/16.png" alt="Screenshot 16" width="50%">

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
@Luar123
@Aximut
Source Code includes the usability of BLUETOOTH thanks to the work of 
@Aximut 

BLUETOOTH is mentioned to have some improvements to be made, I'm sure to advise end users of potential issues highlighted by code creator, although edits further edits have been made by @BakersHub (myself) to eliminate such issues, as of time of writing, no issues have arisen since my edits have been made. but with 1 person debugging it's not ruled out of any issues been eliminated. please use and report any issues with detail for increased chance of fixes been made ASAP.