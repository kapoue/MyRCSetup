# Changelog v1.6.3 - Gradle Configuration Fix

## 🐛 Bug Fixes

### Build System Configuration Fix
- **Added serialization plugin to root build.gradle.kts**: The `org.jetbrains.kotlin.plugin.serialization` plugin version 1.9.20 is now correctly declared at project level
- **Fixed Android Studio compilation error**: Resolved "Plugin was not found in any of the following sources" during Gradle sync

## 📱 Technical Information

### Build System Changes
- Added `org.jetbrains.kotlin.plugin.serialization` plugin version 1.9.20 to root `build.gradle.kts` file
- Updated application version to 1.6.3 (versionCode 15)
- Updated "About" screen with new version information

### Gradle Architecture
- **Project level**: Serialization plugin declaration with version
- **Application level**: Plugin application without version (inherited from project level)
- **Complete configuration**: Gradle sync now functional

## 🔧 User Impact

This version fixes a Gradle configuration issue that prevented compilation in Android Studio. Developers can now:

1. **Open the project** in Android Studio without sync errors
2. **Compile the application** directly from the IDE
3. **Use all export/import JSON features** 
4. **Debug and test** the application in development mode

## 📋 Deployment Notes

- **Recommended update**: Essential for Android Studio compilation
- **Compatibility**: Maintains compatibility with all previous versions
- **Development**: Facilitates development and future contributions

---

**Version**: 1.6.3  
**Version Code**: 15  
**Release Date**: September 24, 2025  
**Platform**: Android 7.0+ (API 24+)