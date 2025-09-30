# BrightWell - Setup Instructions

## Prerequisites
- Android Studio (Arctic Fox or later)
- JDK 8 or higher
- Android SDK with API Level 24+ (Android 7.0)
- Gradle 8.2

## Step-by-Step Setup

### 1. Open Project in Android Studio
1. Launch Android Studio
2. Click "Open an Existing Project"
3. Navigate to the `BrightWell` folder
4. Click "OK"

### 2. Configure SDK Location
1. Create a file named `local.properties` in the project root (if not exists)
2. Add the following line (adjust path to your SDK location):
   ```
   sdk.dir=C\:\\Users\\YourUsername\\AppData\\Local\\Android\\Sdk
   ```
   Or on Mac/Linux:
   ```
   sdk.dir=/Users/YourUsername/Library/Android/sdk
   ```

### 3. Sync Gradle
1. Wait for Android Studio to detect the project
2. Click "Sync Now" when prompted
3. Wait for Gradle sync to complete (may take a few minutes)
4. Resolve any dependency issues if prompted

### 4. Build the Project
1. Click "Build" > "Make Project" (Ctrl+F9 / Cmd+F9)
2. Wait for build to complete
3. Check "Build" tab for any errors

### 5. Run the App

#### On Emulator:
1. Click "Device Manager" in Android Studio
2. Create a new virtual device if needed (recommended: Pixel 5, API 33)
3. Start the emulator
4. Click "Run" button (Shift+F10 / Ctrl+R)
5. Select your emulator from the list

#### On Physical Device:
1. Enable Developer Options on your Android device:
   - Go to Settings > About Phone
   - Tap "Build Number" 7 times
2. Enable USB Debugging:
   - Go to Settings > Developer Options
   - Enable "USB Debugging"
3. Connect device via USB
4. Click "Run" button
5. Select your device from the list

### 6. Grant Permissions (Android 13+)
When the app first launches on Android 13 or higher:
1. Go to Settings tab
2. Enable "Hydration Reminders"
3. Grant notification permission when prompted

### 7. Add Widget to Home Screen (Optional)
1. Long press on your home screen
2. Tap "Widgets"
3. Scroll to find "BrightWell"
4. Drag "Habit Progress" widget to home screen
5. The widget will show your daily progress

## Troubleshooting

### Gradle Sync Failed
- Check internet connection
- Click "File" > "Invalidate Caches" > "Invalidate and Restart"
- Delete `.gradle` folder and sync again

### SDK Not Found
- Ensure `local.properties` has correct SDK path
- Install Android SDK through Android Studio SDK Manager

### Build Errors
- Ensure you have JDK 8 or higher
- Check that all dependencies downloaded successfully
- Try "Build" > "Clean Project" then "Rebuild Project"

### App Crashes on Launch
- Check logcat for error messages
- Ensure minimum SDK version is 24 (Android 7.0)
- Clear app data and try again

### Notifications Not Working
- Grant notification permission in app settings
- Check device notification settings
- Ensure battery optimization is disabled for the app

### Widget Not Updating
- Remove and re-add the widget
- Check habits and verify they're being saved
- Restart the device

## Testing Checklist

Before submitting, verify:
- [ ] App builds without errors
- [ ] All three tabs (Habits, Mood, Settings) are accessible
- [ ] Can add, edit, and delete habits
- [ ] Habit completion persists after app restart
- [ ] Can log mood entries with emojis
- [ ] Can share mood summary
- [ ] Hydration reminders can be enabled/disabled
- [ ] Notifications appear at set intervals
- [ ] Widget displays on home screen
- [ ] Widget updates when habits change
- [ ] App works in portrait and landscape
- [ ] No crashes or ANRs

## Project Files Overview

### Source Code
- `app/src/main/java/` - All Kotlin source files
- `app/src/main/res/` - All resource files (layouts, drawables, etc.)
- `app/src/main/AndroidManifest.xml` - App configuration

### Build Files
- `build.gradle.kts` - Project-level build configuration
- `app/build.gradle.kts` - App-level build configuration
- `settings.gradle.kts` - Project settings

### Documentation
- `README.md` - Project overview and features
- `REPORT.md` - Detailed lab report
- `SETUP_INSTRUCTIONS.md` - This file

## Support

If you encounter issues:
1. Check the error message in logcat
2. Review the troubleshooting section above
3. Ensure all prerequisites are met
4. Try cleaning and rebuilding the project

## Submission Preparation

1. **Clean the project:**
   - Build > Clean Project
   - Delete `build/` folders

2. **Create ZIP file:**
   - Include entire project folder
   - Ensure all source files are included
   - Include README.md and REPORT.md

3. **Convert REPORT.md to PDF:**
   - Use a Markdown to PDF converter
   - Or open in VS Code and export as PDF
   - Include in submission

4. **Take screenshots:**
   - All main screens
   - Dialogs
   - Widget on home screen
   - Notification
   - Add to report

Good luck with your lab exam! 🌟
