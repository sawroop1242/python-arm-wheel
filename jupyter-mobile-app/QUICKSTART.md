# Quick Start Guide - Jupyter Mobile

Get up and running with Jupyter Mobile in 5 minutes!

## Step 1: Start Jupyter Server (On Your Computer)

Open a terminal and run:

```bash
# For Jupyter Notebook
jupyter notebook --ip=0.0.0.0

# OR for JupyterLab
jupyter lab --ip=0.0.0.0
```

**Important**: Copy the token from the output. It looks like:
```
http://localhost:8888/?token=abc123def456ghi789...
```

## Step 2: Find Your Connection Details

### Using Android Emulator?
- **URL**: `http://10.0.2.2:8888`
- **Token**: (paste the token from Step 1)

### Using Physical Android Device?

1. Find your computer's IP address:

   **Windows**:
   ```cmd
   ipconfig
   ```
   Look for "IPv4 Address" (e.g., 192.168.1.100)

   **Mac/Linux**:
   ```bash
   ifconfig | grep "inet "
   # or
   ip addr show
   ```
   Look for your local IP (e.g., 192.168.1.100)

2. Use this configuration:
   - **URL**: `http://YOUR_IP:8888` (e.g., `http://192.168.1.100:8888`)
   - **Token**: (paste the token from Step 1)

## Step 3: Build & Install the App

### Option A: Using Android Studio (Recommended)
1. Open Android Studio
2. Open the `jupyter-mobile-app` folder
3. Connect your device or start an emulator
4. Click the green "Run" button ▶️

### Option B: Using Command Line
```bash
cd jupyter-mobile-app

# Build the APK
./gradlew assembleDebug

# Install on connected device
./gradlew installDebug
```

## Step 4: Configure & Connect

1. Open **Jupyter Mobile** app on your Android device
2. Tap the **Settings** icon (⚙️) in the top-right
3. Enter your **Server URL** and **Token**
4. Tap **Save**
5. Tap **Connect**

🎉 **You're connected!** Start using Jupyter on your mobile device.

## Quick Tips

### Navigation
- **Back/Forward**: Use bottom navigation buttons
- **Refresh**: Tap refresh icon if page doesn't load
- **Switch Interface**: Use Lab/Notebook icons to switch

### Gestures
- **Zoom**: Pinch to zoom in/out
- **Pan**: Two-finger drag when zoomed
- **Fullscreen**: Tap fullscreen icon for more space

### Troubleshooting

**Can't connect?**
1. ✅ Check Jupyter is running on your computer
2. ✅ Verify you're using the correct IP address
3. ✅ Ensure both devices are on the same WiFi network
4. ✅ Check your firewall isn't blocking port 8888
5. ✅ Try copying the token again (no extra spaces)

**UI looks weird?**
- Try pinch-to-zoom
- Switch to fullscreen mode
- Rotate to landscape orientation

**Still having issues?**
- Check the full README.md for detailed troubleshooting
- Restart both Jupyter server and the app
- Try using `0.0.0.0` instead of `localhost` when starting Jupyter

## Next Steps

- Explore JupyterLab vs Notebook interfaces
- Try fullscreen mode for focused coding
- Create and run your first mobile notebook!

---

Need more help? Check out the full [README.md](README.md) for detailed documentation.
