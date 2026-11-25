Backend autostart and persistent-run instructions

1) Quick start (keep running, auto-restarts on crash)

- Double-click `run-backend.bat` (Windows) or run `run-backend.ps1` in PowerShell.
- The scripts run `java -cp <repo>\backend\bin app.Main` and will restart the server if it exits.

2) Run at Windows login

- Create a shortcut to `run-backend.bat` and place it in your Startup folder:

  - Press `Win+R`, paste `shell:startup`, and press Enter.
  - Copy the shortcut into the opened folder.

3) Notes

- Ensure Java is installed and `java` is available on `PATH`.
- The scripts expect compiled classes under `backend\bin` (this repo already contains `.class` files).
- If you want npm to start the backend automatically when running the frontend, tell me and I'll add an npm script using the batch runner.
