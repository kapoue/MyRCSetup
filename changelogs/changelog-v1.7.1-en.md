# Changelog v1.7.1 - Critical Bug Fixes

**Release Date:** September 24, 2025

## 🐛 Bug Fixes

### User Interface
- **Automatic lap time formatting**: The "Best lap time" field now automatically applies mm:ss.ms format during input
- **Double-click fix**: Resolved bug that prevented viewing a session by clicking on its card
- **Unsaved changes dialog**: Fixed crashes when using "Save", "Exit without saving" and "Cancel" actions
- **Android back button handling**: System back button now properly displays confirmation dialog for unsaved changes

### QR Code Features
- **Automatic sharing**: QR codes are now automatically shared upon generation, without additional action required
- **Simplified import**: QR Code import now goes directly to camera without intermediate screen
- **Portrait mode**: QR Code scanner is now locked to portrait orientation for better experience

### Versions and Metadata
- **Version consistency**: Fixed all version number inconsistencies throughout the application
- **JSON export**: Correct version (1.7.1) is now displayed in JSON exports
- **About screen**: Updated to version 1.7.1 and version code 18

## 🔧 Technical Improvements

### Data Validation
- **Character filtering**: Lap time field now only accepts digits, colons and periods
- **Smart formatting**: Automatic mm:ss.ms format application during input

### Error Handling
- **Crash protection**: Added try-catch blocks in dialog actions to prevent crashes
- **Exception management**: Improved UI robustness

### Navigation
- **BackHandler**: Native Android back button handler integration
- **User flow**: Improved consistency in screen navigation

## 📱 Compatibility

- **Minimum Android**: 7.0 (API 24)
- **Target Android**: 14 (API 35)
- **Architecture**: ARM64, ARM32, x86_64

## 🔄 Migration

This version is fully compatible with data from previous versions. No specific action is required when updating.

## 📋 Technical Notes

- **Version code**: 18
- **APK size**: ~8 MB
- **Permissions**: Camera (for QR codes), Storage (for import/export)

---

**Installation:** Download APK from [GitHub releases](https://github.com/kapoue/MyRCSetup/releases) or compile from source code.