# Jupyter Mobile - Android Client for Jupyter Notebooks

A native Android application that provides a mobile-friendly interface to connect to Jupyter Notebook and JupyterLab servers running on localhost or local network.

## Features

✨ **Core Features**
- 📱 Native Android app built with Kotlin and Jetpack Compose
- 🔗 Connect to Jupyter Notebook and JupyterLab servers
- 🌐 Support for localhost and network connections
- 🔐 Token-based authentication
- 📲 Mobile-optimized UI with touch gestures
- 🎯 Pinch-to-zoom and pan support
- 📐 Responsive layout for all screen sizes
- 🌓 Material Design 3 with dark/light theme support

🎨 **Mobile Optimizations**
- Custom CSS injection for better mobile experience
- Larger touch targets for buttons and controls
- Optimized font sizes for mobile screens
- Sticky headers for better navigation
- Full-screen mode for distraction-free coding

🛠️ **Navigation & Controls**
- Bottom navigation bar with quick actions
- Back/Forward navigation
- Refresh page
- Switch between Notebook and Lab interfaces
- Settings management

## Requirements

### For Building
- Android Studio Arctic Fox or later
- JDK 8 or higher
- Android SDK with API level 24+ (Android 7.0+)
- Gradle 8.0+

### For Running
- Android device or emulator with Android 7.0 (API 24) or higher
- Jupyter Notebook or JupyterLab server running on:
  - Your computer (localhost)
  - Local network
  - Accessible via IP address

## Installation

### Option 1: Build from Source

1. **Clone the repository**
   ```bash
   cd jupyter-mobile-app
   ```

2. **Open in Android Studio**
   - Open Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `jupyter-mobile-app` folder
   - Wait for Gradle sync to complete

3. **Build the APK**
   ```bash
   ./gradlew assembleDebug
   ```
   
   The APK will be generated at:
   ```
   app/build/outputs/apk/debug/app-debug.apk
   ```

4. **Install on Device**
   - Connect your Android device via USB (with USB debugging enabled)
   - Run:
     ```bash
     ./gradlew installDebug
     ```
   - Or manually install the APK from the build output folder

### Option 2: Using Android Studio

1. Open the project in Android Studio
2. Connect your Android device or start an emulator
3. Click the "Run" button (green play icon) or press Shift+F10
4. Select your device and click OK

## Setup & Configuration

### 1. Start Jupyter Server

On your computer, start Jupyter with network access:

**For Jupyter Notebook:**
```bash
jupyter notebook --ip=0.0.0.0 --port=8888
```

**For JupyterLab:**
```bash
jupyter lab --ip=0.0.0.0 --port=8888
```

**Note the token** from the terminal output. It will look like:
```
http://localhost:8888/?token=abc123def456...
```

### 2. Configure the App

#### For Localhost (Android Emulator)
If you're using an Android emulator, use the special IP:
- **Server URL**: `http://10.0.2.2:8888`
- **Token**: Copy from Jupyter terminal output

#### For Physical Device (Same Network)
If you're using a physical Android device on the same network:
1. Find your computer's IP address:
   - **Windows**: `ipconfig` (look for IPv4 Address)
   - **Mac/Linux**: `ifconfig` or `ip addr` (look for inet)
   
2. Configure the app:
   - **Server URL**: `http://YOUR_COMPUTER_IP:8888`
   - **Token**: Copy from Jupyter terminal output

Example: `http://192.168.1.100:8888`

### 3. Connect

1. Open the Jupyter Mobile app
2. Tap the **Settings** icon (gear icon)
3. Enter your **Server URL** and **Token**
4. Tap **Save**
5. Tap **Connect** on the main screen

## Usage Guide

### Main Screen
- **Connect Button**: Establishes connection to Jupyter server
- **Settings Icon**: Configure server URL and token
- **Fullscreen Icon**: Toggle fullscreen mode

### Bottom Navigation (When Connected)
- **Back Arrow**: Navigate to previous page
- **Forward Arrow**: Navigate to next page
- **Refresh**: Reload current page
- **Lab Icon**: Switch to JupyterLab interface
- **Notebook Icon**: Switch to Jupyter Notebook interface

### Gestures
- **Pinch to Zoom**: Zoom in/out on notebook content
- **Two-finger Pan**: Pan around zoomed content
- **Swipe**: Scroll through notebooks
- **Long Press**: Context menu (where supported)

### Fullscreen Mode
- Tap the fullscreen icon to hide toolbars
- Tap the exit fullscreen button (top-right) to restore toolbars

## Troubleshooting

### Connection Issues

**Problem**: "Connection Failed" error

**Solutions**:
1. **Check Jupyter is running**: Ensure Jupyter server is started on your computer
2. **Verify IP address**: Make sure you're using the correct IP
   - Emulator: Use `10.0.2.2` instead of `localhost`
   - Physical device: Use your computer's local IP (e.g., `192.168.1.100`)
3. **Check firewall**: Ensure your firewall allows connections on port 8888
4. **Verify token**: Copy the complete token from Jupyter terminal
5. **Network connectivity**: Ensure both devices are on the same network

### Display Issues

**Problem**: Text is too small or UI is cramped

**Solutions**:
1. Use pinch-to-zoom gesture to adjust size
2. Try fullscreen mode for more space
3. Rotate device to landscape orientation

**Problem**: Jupyter UI not responsive

**Solutions**:
1. Tap the refresh button
2. Try switching between Lab and Notebook interfaces
3. Disconnect and reconnect

### Performance Issues

**Problem**: App is slow or laggy

**Solutions**:
1. Close other apps to free up memory
2. Restart the app
3. Use Jupyter Notebook instead of Lab (lighter weight)
4. Clear app cache in Android settings

## Network Security

The app is configured to allow cleartext HTTP traffic for localhost connections. For production use or public networks, consider:

1. **Using HTTPS**: Configure Jupyter with SSL certificates
2. **VPN**: Use a VPN for secure remote access
3. **SSH Tunneling**: Create an SSH tunnel to your Jupyter server

## Project Structure

```
jupyter-mobile-app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/jupyter/mobile/
│   │       │   ├── MainActivity.kt              # Main activity
│   │       │   ├── data/
│   │       │   │   ├── ConnectionState.kt       # Connection states
│   │       │   │   └── JupyterPreferences.kt    # Settings storage
│   │       │   ├── ui/
│   │       │   │   ├── components/
│   │       │   │   │   └── JupyterWebView.kt    # WebView component
│   │       │   │   ├── screens/
│   │       │   │   │   ├── MainScreen.kt        # Main UI screen
│   │       │   │   │   └── SettingsScreen.kt    # Settings UI
│   │       │   │   └── theme/
│   │       │   │       └── Theme.kt             # Material theme
│   │       │   └── viewmodel/
│   │       │       └── JupyterViewModel.kt      # App state management
│   │       ├── res/                             # Resources
│   │       └── AndroidManifest.xml              # App manifest
│   └── build.gradle.kts                         # App build config
├── gradle/                                       # Gradle wrapper
├── build.gradle.kts                             # Project build config
├── settings.gradle.kts                          # Project settings
└── README.md                                    # This file
```

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Design**: Material Design 3
- **Architecture**: MVVM (Model-View-ViewModel)
- **WebView**: Android WebView with custom optimizations
- **Storage**: DataStore for preferences
- **Navigation**: Jetpack Navigation Compose
- **Build System**: Gradle with Kotlin DSL

## Development

### Building for Release

1. **Generate a signing key**:
   ```bash
   keytool -genkey -v -keystore my-release-key.jks -keyalg RSA -keysize 2048 -validity 10000 -alias my-key-alias
   ```

2. **Configure signing** in `app/build.gradle.kts`:
   ```kotlin
   android {
       signingConfigs {
           create("release") {
               storeFile = file("my-release-key.jks")
               storePassword = "your-password"
               keyAlias = "my-key-alias"
               keyPassword = "your-password"
           }
       }
       buildTypes {
           release {
               signingConfig = signingConfigs.getByName("release")
           }
       }
   }
   ```

3. **Build release APK**:
   ```bash
   ./gradlew assembleRelease
   ```

### Running Tests

```bash
# Unit tests
./gradlew test

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

### Code Style

This project follows the official Kotlin coding conventions. Format code using:
```bash
./gradlew ktlintFormat
```

## Contributing

Contributions are welcome! Please feel free to submit issues or pull requests.

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Built with [Jetpack Compose](https://developer.android.com/jetpack/compose)
- Uses [Material Design 3](https://m3.material.io/)
- Inspired by the Jupyter project

## Support

For issues, questions, or suggestions:
- Open an issue on GitHub
- Check existing issues for solutions
- Review the troubleshooting section above

## Roadmap

Future enhancements planned:
- [ ] File upload/download support
- [ ] Code syntax highlighting improvements
- [ ] Offline notebook viewing
- [ ] Multiple server profiles
- [ ] Keyboard shortcuts customization
- [ ] Tablet-optimized layout
- [ ] Widget support for quick access
- [ ] Share notebooks via Android share sheet

---

**Made with ❤️ for mobile Jupyter users**
