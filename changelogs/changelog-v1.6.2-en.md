# Changelog v1.6.2 - Serialization Fix

## 🐛 Bug Fixes

### Critical Export/Import Fix
- **Added missing Kotlin serialization plugin**: Fixed "Serializer for class 'ExportData' is not found" error that prevented data export
- **Complete serialization system configuration**: The `org.jetbrains.kotlin.plugin.serialization` plugin is now properly configured in the build system

## 📱 Technical Information

### Build System Changes
- Added `org.jetbrains.kotlin.plugin.serialization` plugin in `build.gradle.kts`
- Updated application version to 1.6.2 (versionCode 14)
- Updated "About" screen with new version information

### Affected Features
- ✅ **JSON Export**: Now functional with native Android sharing
- ✅ **JSON Import**: Native Android file picker operational
- ✅ **Data serialization**: Correct conversion of RC sessions to JSON format

## 🔧 User Impact

This version fixes a critical issue that completely prevented the use of export and import features introduced in v1.6.1. Users can now:

1. **Export their sessions** via the "Export" button in the menu
2. **Share their data** with other applications (email, cloud, etc.)
3. **Import sessions** from existing JSON files
4. **Backup their settings** to restore them later

## 📋 Deployment Notes

- **Recommended update**: This fix is essential for all v1.6.1 users
- **Compatibility**: Maintains compatibility with all previous versions
- **Data**: No database migration required

---

**Version**: 1.6.2  
**Version Code**: 14  
**Release Date**: September 24, 2025  
**Platform**: Android 7.0+ (API 24+)