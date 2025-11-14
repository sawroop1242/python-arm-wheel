#!/bin/bash

# Jupyter Mobile - Build Verification Script
# This script checks if all required files are present and the project structure is correct

echo "🔍 Verifying Jupyter Mobile project structure..."
echo ""

# Color codes
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

errors=0
warnings=0

# Function to check if file exists
check_file() {
    if [ -f "$1" ]; then
        echo -e "${GREEN}✓${NC} $1"
    else
        echo -e "${RED}✗${NC} $1 - MISSING"
        ((errors++))
    fi
}

# Function to check if directory exists
check_dir() {
    if [ -d "$1" ]; then
        echo -e "${GREEN}✓${NC} $1/"
    else
        echo -e "${RED}✗${NC} $1/ - MISSING"
        ((errors++))
    fi
}

echo "📁 Checking project structure..."
check_file "build.gradle.kts"
check_file "settings.gradle.kts"
check_file "gradle.properties"
check_file "gradlew"
check_dir "app"
check_dir "gradle/wrapper"
echo ""

echo "📱 Checking app module..."
check_file "app/build.gradle.kts"
check_file "app/proguard-rules.pro"
check_file "app/src/main/AndroidManifest.xml"
echo ""

echo "🎨 Checking resources..."
check_file "app/src/main/res/values/strings.xml"
check_file "app/src/main/res/values/themes.xml"
check_file "app/src/main/res/drawable/ic_launcher.xml"
check_file "app/src/main/res/xml/network_security_config.xml"
check_file "app/src/main/res/xml/backup_rules.xml"
check_file "app/src/main/res/xml/data_extraction_rules.xml"
echo ""

echo "💻 Checking Kotlin source files..."
check_file "app/src/main/java/com/jupyter/mobile/MainActivity.kt"
check_file "app/src/main/java/com/jupyter/mobile/data/ConnectionState.kt"
check_file "app/src/main/java/com/jupyter/mobile/data/JupyterPreferences.kt"
check_file "app/src/main/java/com/jupyter/mobile/viewmodel/JupyterViewModel.kt"
check_file "app/src/main/java/com/jupyter/mobile/ui/theme/Theme.kt"
check_file "app/src/main/java/com/jupyter/mobile/ui/components/JupyterWebView.kt"
check_file "app/src/main/java/com/jupyter/mobile/ui/screens/MainScreen.kt"
check_file "app/src/main/java/com/jupyter/mobile/ui/screens/SettingsScreen.kt"
echo ""

echo "📚 Checking documentation..."
check_file "README.md"
check_file "QUICKSTART.md"
check_file "BUILD_INSTRUCTIONS.md"
echo ""

# Check if gradlew is executable
if [ -x "gradlew" ]; then
    echo -e "${GREEN}✓${NC} gradlew is executable"
else
    echo -e "${YELLOW}⚠${NC} gradlew is not executable (run: chmod +x gradlew)"
    ((warnings++))
fi
echo ""

# Summary
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
if [ $errors -eq 0 ] && [ $warnings -eq 0 ]; then
    echo -e "${GREEN}✓ All checks passed!${NC}"
    echo ""
    echo "🚀 Ready to build! Run one of these commands:"
    echo "   ./gradlew assembleDebug        # Build debug APK"
    echo "   ./gradlew installDebug         # Build and install"
    echo ""
    echo "Or open the project in Android Studio."
    exit 0
elif [ $errors -eq 0 ]; then
    echo -e "${YELLOW}⚠ Verification completed with $warnings warning(s)${NC}"
    echo ""
    echo "You can proceed with building, but check the warnings above."
    exit 0
else
    echo -e "${RED}✗ Verification failed with $errors error(s) and $warnings warning(s)${NC}"
    echo ""
    echo "Please fix the errors above before building."
    exit 1
fi
