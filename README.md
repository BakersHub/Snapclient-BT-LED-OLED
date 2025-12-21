# Snapclient-BT-LED-OLED
Snapclient & Bluetooth receiver with inbuilt LED ARGB sound reactive controller

Screenshots are coming

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
