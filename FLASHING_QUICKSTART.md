# Quick Start: Flashing and Installing Snapclient BT + LED + OLED

These are the simplest steps to get the prebuilt firmware running on an ESP32‑WROVER and connected to your Snapserver.

---

## 1. Flash the prebuilt firmware

- Board: **ESP32‑WROVER** (or compatible devkit).
- Firmware: **SnapClient-BT-LED-OLED.bin**.
- Flash tool: any ESP32 flasher, for example **ESPHome Flasher** or the **ESPHome Web Flasher**.

Steps:

1. Connect the WROVER board to your PC via USB.
2. Open your ESPHome flasher tool and select the correct serial port.
3. Choose the `SnapClient-BT-LED-OLEDv1.x.bin` file.
4. Start the flash and wait until it finishes successfully.

The board will reboot automatically after flashing.

---

## 2. Wait for the ESP32-SNAPCLIENT-****** Wi‑Fi access point

- After the first reboot, the firmware will try to connect to Wi‑Fi using any stored credentials.
- On a fresh flash (or when credentials are wrong/unavailable), it will **fail a few times and then start its own AP**.

Within about **5–10 seconds** you should see a new open Wi‑Fi network:

- SSID: **ESP32-SNAPCLIENT-******** (or similar)

Use any device (phone, tablet, laptop) to:

1. Open the Wi‑Fi settings.
2. Connect to the open `ESP32-SNAPCLIENT-******` AP.

---

## 3. Open the configuration page

On **Android** this is usually the easiest:

1. After connecting to the `ESP32-SNAPCLIENT-******` AP, Android will pop up a **“Sign in to network”** / **captive portal** window.
2. Allow it to connect; if the captive portal opens, you are already on the setup page.
3. If not, tap the **gear icon** next to the `ESP32-SNAPCLIENT-******` network in Wi‑Fi settings.
4. Tap **Manage router** (or similar) — this opens the device config page.

On any device, you can also open a browser and go directly to:

- `http://192.168.4.1` (the default AP gateway address)

---

## 4. Fill in the basic Snapclient settings

On the config/setup page:

1. Scroll down to the **Wi‑Fi** section and enter:
   - **Wi‑Fi SSID** (your home/office network name).
   - **Wi‑Fi password**.
2. Scroll to the **Snapserver** / **Snapclient** section and enter:
   - **Snapserver host** (IP or hostname where Snapserver is running).
   - **Snapclient / Speaker name** (how this device should appear in Snapserver).
3. Press **Save and Restart**.

These fields (Wi‑Fi SSID, password, Snapserver host, and speaker name) are the minimum needed for a working Snapclient connection.

After reboot:

- The device will connect to your Wi‑Fi using the new credentials.
- It will connect to the Snapserver at the host/port you specified.

If everything is correct and your Snapserver is running on the same network with the correct port configured, you should now see this device appear as a **speaker/client** in the Snapserver UI.

**Finished!** Your Snapclient BT + LED + OLED build is now flashed, configured, and ready to use.
