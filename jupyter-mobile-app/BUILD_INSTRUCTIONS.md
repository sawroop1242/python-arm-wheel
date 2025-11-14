# Build Instructions - Jupyter Mobile

Detailed instructions for building the Jupyter Mobile Android app.

## Prerequisites

### Required Software

1. **Java Development Kit (JDK)**
   - JDK 8 or higher (JDK 11 recommended)
   - Download from: https://adoptium.net/

2. **Android Studio** (Recommended)
   - Download from: https://developer.android.com/studio
   - Version: Arctic Fox (2020.3.1) or later

3. **Android SDK**
   - Installed automatically with Android Studio
   - Or install via command line tools

4. **Gradle**
   - Included via Gradle Wrapper (no separate installation needed)

### System Requirements

- **OS**: Windows 10/11, macOS 10.14+, or Linux
- **RAM**: 8 GB minimum (16 GB recommended)
- **Disk Space**: 10 GB free space
- **Internet**: Required for downloading dependencies

## Build Methods

### Method 1: Android Studio (Easiest)

#### 1. Install Android Studio

Download and install from: https://developer.android.com/studio

#### 2. Open Project

1. Launch Android Studio
2. Select **"Open an Existing Project"**
3. Navigate to `jupyter-mobile-app` folder
4. Click **"OK"**

#### 3. Sync Project

- Android Studio will automatically start syncing Gradle
- Wait for "Gradle sync finished" message
- If prompted, accept any SDK licenses

#### 4. Build APK

**For Debug Build:**
1. Go to **Build → Build Bundle(s) / APK(s) → Build APK(s)**
2. Wait for build to complete
3. Click **"locate"** in the notification to find the APK

**For Release Build:**
1. Go to **Build → Generate Signed Bundle / APK**
2. Select **APK** and click **Next**
3. Create or select a keystore
4. Fill in keystore details
5. Click **Next** and **Finish**

#### 5. Install on Device

**Using Android Studio:**
1. Connect your Android device via USB
2. Enable USB debugging on your device
3. Click the green **Run** button (▶️)
4. Select your device from the list
5. Click **OK**

### Method 2: Command Line (Advanced)

#### 1. Setup Environment

**Set ANDROID_HOME environment variable:**

**Linux/macOS:**
```bash
export ANDROID_HOME=$HOME/Android/Sdk
export PATH=$PATH:$ANDROID_HOME/tools:$ANDROID_HOME/platform-tools
```

**Windows (PowerShell):**
```powershell
$env:ANDROID_HOME = "$env:LOCALAPPDATA\Android\Sdk"
$env:PATH += ";$env:ANDROID_HOME\tools;$env:ANDROID_HOME\platform-tools"
```

#### 2. Navigate to Project

```bash
cd jupyter-mobile-app
```

#### 3. Make Gradlew Executable (Linux/macOS)

```bash
chmod +x gradlew
```

#### 4. Build APK

**Debug Build:**
```bash
./gradlew assembleDebug
```

**Release Build (unsigned):**
```bash
./gradlew assembleRelease
```

#### 5. Locate APK

**Debug APK:**
```
app/build/outputs/apk/debug/app-debug.apk
```

**Release APK:**
```
app/build/outputs/apk/release/app-release.apk
```

#### 6. Install on Device

**Using ADB:**
```bash
# Connect device via USB with USB debugging enabled
adb devices  # Verify device is connected

# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Or use Gradle
./gradlew installDebug
```

## Build Variants

### Debug Build
- Includes debugging information
- Not optimized
- Larger APK size
- Can be installed alongside release builds
- **Use for**: Development and testing

```bash
./gradlew assembleDebug
```

### Release Build
- Optimized and minified
- Smaller APK size
- Requires signing for installation
- **Use for**: Production distribution

```bash
./gradlew assembleRelease
```

## Signing the APK (For Release)

### 1. Generate Keystore

```bash
keytool -genkey -v -keystore my-release-key.jks \
  -keyalg RSA -keysize 2048 -validity 10000 \
  -alias my-key-alias
```

Follow the prompts to set passwords and details.

### 2. Configure Signing

Create `keystore.properties` in project root:

```properties
storePassword=your_store_password
keyPassword=your_key_password
keyAlias=my-key-alias
storeFile=my-release-key.jks
```

**Important**: Add `keystore.properties` to `.gitignore`!

### 3. Update build.gradle.kts

Add to `app/build.gradle.kts`:

```kotlin
// Load keystore properties
val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties()
if (keystorePropertiesFile.exists()) {
    keystoreProperties.load(FileInputStream(keystorePropertiesFile))
}

android {
    signingConfigs {
        create("release") {
            keyAlias = keystoreProperties["keyAlias"] as String
            keyPassword = keystoreProperties["keyPassword"] as String
            storeFile = file(keystoreProperties["storeFile"] as String)
            storePassword = keystoreProperties["storePassword"] as String
        }
    }
    
    buildTypes {
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}
```

### 4. Build Signed APK

```bash
./gradlew assembleRelease
```

## Troubleshooting Build Issues

### Gradle Sync Failed

**Problem**: Gradle sync fails with dependency errors

**Solution**:
```bash
# Clean project
./gradlew clean

# Refresh dependencies
./gradlew --refresh-dependencies

# Rebuild
./gradlew build
```

### SDK Not Found

**Problem**: "SDK location not found"

**Solution**:
Create `local.properties` in project root:
```properties
sdk.dir=/path/to/your/Android/Sdk
```

**Common SDK locations:**
- **Windows**: `C:\Users\YourName\AppData\Local\Android\Sdk`
- **macOS**: `/Users/YourName/Library/Android/sdk`
- **Linux**: `/home/YourName/Android/Sdk`

### Out of Memory

**Problem**: Build fails with "Out of memory" error

**Solution**:
Edit `gradle.properties`:
```properties
org.gradle.jvmargs=-Xmx4096m -XX:MaxPermSize=1024m
```

### Build Tools Version

**Problem**: "Failed to find Build Tools revision X.X.X"

**Solution**:
```bash
# List installed build tools
sdkmanager --list

# Install required version
sdkmanager "build-tools;34.0.0"
```

### License Not Accepted

**Problem**: "You have not accepted the license agreements"

**Solution**:
```bash
# Accept all licenses
yes | sdkmanager --licenses
```

## Build Optimization

### Reduce Build Time

1. **Enable Gradle Daemon** (in `gradle.properties`):
   ```properties
   org.gradle.daemon=true
   org.gradle.parallel=true
   org.gradle.caching=true
   ```

2. **Use Build Cache**:
   ```bash
   ./gradlew build --build-cache
   ```

3. **Increase Heap Size** (in `gradle.properties`):
   ```properties
   org.gradle.jvmargs=-Xmx4096m
   ```

### Reduce APK Size

1. **Enable ProGuard/R8** (already configured for release)
2. **Remove unused resources**:
   ```kotlin
   android {
       buildTypes {
           release {
               isShrinkResources = true
               isMinifyEnabled = true
           }
       }
   }
   ```

3. **Use APK Analyzer** in Android Studio:
   - Build → Analyze APK
   - Identify large files/resources

## Continuous Integration

### GitHub Actions Example

Create `.github/workflows/build.yml`:

```yaml
name: Android Build

on:
  push:
    branches: [ main ]
  pull_request:
    branches: [ main ]

jobs:
  build:
    runs-on: ubuntu-latest
    
    steps:
    - uses: actions/checkout@v3
    
    - name: Set up JDK 11
      uses: actions/setup-java@v3
      with:
        java-version: '11'
        distribution: 'temurin'
    
    - name: Grant execute permission for gradlew
      run: chmod +x gradlew
    
    - name: Build with Gradle
      run: ./gradlew assembleDebug
    
    - name: Upload APK
      uses: actions/upload-artifact@v3
      with:
        name: app-debug
        path: app/build/outputs/apk/debug/app-debug.apk
```

## Next Steps

After building:
1. Install the APK on your device
2. Follow the [QUICKSTART.md](QUICKSTART.md) guide
3. Configure your Jupyter server connection
4. Start using Jupyter Mobile!

## Need Help?

- Check the main [README.md](README.md)
- Review Android Studio documentation
- Check Gradle build logs for detailed errors
- Ensure all prerequisites are installed correctly

---

Happy Building! 🚀
