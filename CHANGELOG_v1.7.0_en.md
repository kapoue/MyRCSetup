# Changelog Version 1.7.0 - "QR Code & UX"

## 🎯 New Features

### 📱 QR Code Sharing
- **QR Code button** in the toolbar for saved sessions
- **Automatic generation** of QR codes to share your settings
- **Direct sharing** of QR codes via installed apps
- **Smart optimization** of QR code size based on data

### 📥 QR Code Import
- **"Import via QR Code" menu** in the main screen
- **Integrated scanner** with camera permission management
- **Automatic validation** of RC session QR codes
- **Direct import** of scanned settings into your collection

### 💾 Confirmation Dialog
- **Protection against loss** of unsaved modifications
- **Smart dialog** when navigating back
- **Flexible options**: Save and exit, Exit without saving, Cancel
- **Automatic detection** of form changes

## 🔧 Technical Improvements

### 📚 New Dependencies
- **ZXing Android Embedded 4.3.0** for QR generation/scanning
- **ZXing Core 3.5.3** for code processing
- **CameraX** for modern camera access

### 🔐 Permissions
- **CAMERA permission** added for QR scanning
- **Graceful handling** of permission denials
- **Informative interface** to guide users

### 🏗️ Architecture
- **QRCodeGenerator**: Generation utility with custom colors
- **QRCodeScanner**: Jetpack Compose composables for scanning
- **Robust validation** of scanned QR data
- **Enhanced state management** in ViewModel

## 📊 Data & Export

### 🔄 Export Version
- **Version 1.7.0** in JSON exports
- **Compatibility** maintained with previous versions
- **Enriched metadata** for version tracking

### 🎨 User Interface
- **QR Code icon** in session TopAppBar
- **Modern and intuitive** scanning interface
- **Explicit and helpful** error messages
- **Visual feedback** for all actions

## 🐛 Bug Fixes

### 🔧 Stability
- **Fixed** QR Code compilation errors
- **Validation** of composable parameters
- **Robust error handling** for QR operations

### 📱 Compatibility
- **Android 7.0+** (API 24) support maintained
- **Optimization** for different screen sizes
- **Improved performance** for QR generation

## 🚀 Usage

### Share a Session
1. Open a saved session
2. Click the QR Code icon in the toolbar
3. Share the generated QR code via your apps

### Import a Session
1. Main menu → "Import via QR Code"
2. Allow camera access if prompted
3. Scan a session QR code
4. The session is automatically imported

### Data Protection
- Your modifications are automatically detected
- A dialog offers to save before exiting
- No accidental data loss

---

**Version**: 1.7.0  
**Version Code**: 16  
**Date**: January 2025  
**Compatibility**: Android 7.0+ (API 24)  
**Size**: ~8 MB