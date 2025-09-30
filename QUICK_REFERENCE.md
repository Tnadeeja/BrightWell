# BrightWell - Quick Reference Guide

## 🚀 Essential Commands

### Build & Run
```bash
# Clean project
./gradlew clean

# Build project
./gradlew build

# Install on device
./gradlew installDebug

# Run tests
./gradlew test
```

### Android Studio Shortcuts
- **Run App**: `Shift + F10` (Windows) / `Ctrl + R` (Mac)
- **Build Project**: `Ctrl + F9` / `Cmd + F9`
- **Clean Project**: Build > Clean Project
- **Rebuild Project**: Build > Rebuild Project
- **Sync Gradle**: File > Sync Project with Gradle Files

---

## 📱 Testing Workflow

### 1. First Launch
1. Open app
2. Navigate to Settings
3. Enable hydration reminders
4. Grant notification permission (Android 13+)
5. Go back to Habits tab

### 2. Test Habits
1. Tap + button
2. Add habit: "Drink Water"
3. Add description: "8 glasses per day"
4. Tap Add
5. Check the habit checkbox
6. Verify progress updates to show percentage
7. Edit habit (tap edit icon)
8. Delete habit (tap delete icon, confirm)

### 3. Test Mood Journal
1. Navigate to Mood tab
2. Tap + button
3. Select an emoji (e.g., 😊)
4. Add note: "Feeling great today!"
5. Tap Save
6. Verify entry appears in list
7. Tap Share button
8. Select sharing app
9. Verify summary is shared

### 4. Test Settings
1. Navigate to Settings tab
2. Toggle hydration reminders off/on
3. Tap "Change" interval
4. Select different interval
5. Tap "About BrightWell"
6. Read app information

### 5. Test Widget
1. Long press home screen
2. Tap Widgets
3. Find "BrightWell - Habit Progress"
4. Drag to home screen
5. Open app and check a habit
6. Go back to home screen
7. Verify widget updated

### 6. Test Persistence
1. Add some habits and moods
2. Close app completely
3. Reopen app
4. Verify all data is still there

### 7. Test Notifications
1. Enable reminders with short interval (15 min)
2. Wait for notification
3. Tap notification
4. Verify app opens

### 8. Test Responsive Design
1. Rotate device to landscape
2. Verify all screens look good
3. Test on tablet (if available)
4. Verify layouts adapt properly

---

## 🐛 Common Issues & Fixes

### Issue: Gradle Sync Failed
**Fix**: 
```
1. File > Invalidate Caches > Invalidate and Restart
2. Delete .gradle folder
3. Sync again
```

### Issue: SDK Not Found
**Fix**:
```
1. Create local.properties file
2. Add: sdk.dir=C:\\Users\\YourName\\AppData\\Local\\Android\\Sdk
3. Sync Gradle
```

### Issue: Build Error - Cannot Find Symbol
**Fix**:
```
1. Build > Clean Project
2. Build > Rebuild Project
3. Sync Gradle
```

### Issue: App Crashes on Launch
**Fix**:
```
1. Check logcat for error
2. Uninstall app from device
3. Rebuild and install
4. Clear app data
```

### Issue: Notifications Not Showing
**Fix**:
```
1. Settings > Apps > BrightWell > Notifications > Enable
2. Check battery optimization settings
3. Grant POST_NOTIFICATIONS permission
4. Restart device
```

### Issue: Widget Not Updating
**Fix**:
```
1. Remove widget from home screen
2. Re-add widget
3. Check a habit in app
4. Wait a few seconds
5. Check widget again
```

---

## 📋 Pre-Submission Checklist

### Code Quality
- [ ] All files have proper package declarations
- [ ] All classes have KDoc comments
- [ ] All methods have documentation
- [ ] No unused imports
- [ ] No compiler warnings
- [ ] Consistent code formatting
- [ ] Meaningful variable names

### Functionality
- [ ] Can add habits
- [ ] Can edit habits
- [ ] Can delete habits
- [ ] Habits persist after restart
- [ ] Progress percentage calculates correctly
- [ ] Can log mood with emoji
- [ ] Can add note to mood
- [ ] Can share mood summary
- [ ] Moods persist after restart
- [ ] Can enable/disable reminders
- [ ] Can change reminder interval
- [ ] Notifications appear
- [ ] Widget displays on home screen
- [ ] Widget updates when habits change
- [ ] No crashes or ANRs

### UI/UX
- [ ] All screens look good
- [ ] Navigation works smoothly
- [ ] Dialogs display correctly
- [ ] Empty states show properly
- [ ] Progress bars animate
- [ ] Buttons have proper feedback
- [ ] Works in portrait mode
- [ ] Works in landscape mode
- [ ] Adapts to different screen sizes

### Documentation
- [ ] README.md complete
- [ ] REPORT.md complete
- [ ] REPORT.md converted to PDF
- [ ] Screenshots taken
- [ ] Screenshots added to report
- [ ] All code commented
- [ ] Setup instructions clear

### Submission
- [ ] Project builds successfully
- [ ] All tests pass
- [ ] APK can be generated
- [ ] Project folder cleaned (no build folders)
- [ ] Project zipped
- [ ] PDF report included
- [ ] File size reasonable (<50MB)
- [ ] Ready for upload

---

## 🎯 Viva Quick Tips

### Opening Statement
"I've developed BrightWell, a personal wellness app with habit tracking, mood journaling, hydration reminders, and a home screen widget. All features are fully functional and the code is well-documented."

### Demo Order
1. Show habits screen - add/edit/delete
2. Show progress card updating
3. Show mood journal - log mood with emoji
4. Share mood summary
5. Show settings - enable reminders
6. Show widget on home screen
7. Rotate device to show responsive design

### Key Points to Mention
- "I used SharedPreferences for data persistence as required"
- "WorkManager ensures reliable notifications"
- "The widget is my advanced feature"
- "Material Design 3 for modern UI"
- "Fragment-based architecture"
- "All code is well-documented"

### If Asked About Improvements
- "I would add Room database for complex queries"
- "Dark mode support"
- "Cloud sync with Firebase"
- "More detailed analytics"
- "Habit streaks and achievements"

---

## 📞 Emergency Contacts

### If Demo Fails
1. Have backup APK ready
2. Have screenshots ready
3. Have video recording ready
4. Explain what should happen

### If Code Question Stumps You
1. "Let me check the documentation"
2. "I would research that approach"
3. "That's a good question, here's my understanding..."
4. Be honest if you don't know

---

## 🎓 Key Concepts to Remember

### Android Basics
- **Activity**: Single screen with UI
- **Fragment**: Reusable portion of UI
- **Intent**: Message to start component
- **Service**: Background operation
- **BroadcastReceiver**: Listens for system events

### Data Storage
- **SharedPreferences**: Key-value pairs
- **Internal Storage**: Private files
- **External Storage**: Public files
- **SQLite**: Structured data (not used here)

### UI Components
- **RecyclerView**: Efficient list display
- **ViewBinding**: Type-safe view access
- **Material Design**: Google's design system
- **ConstraintLayout**: Flexible layouts

### Background Tasks
- **WorkManager**: Deferrable background work
- **AlarmManager**: Scheduled tasks
- **JobScheduler**: System job scheduling
- **Foreground Service**: Visible background work

---

## 📊 Project Metrics

### Lines of Code
- Kotlin: ~2,000 lines
- XML: ~1,500 lines
- Total: ~3,500 lines

### Files Created
- Kotlin files: 13
- Layout files: 12
- Drawable files: 6
- Other resources: 10+
- Documentation: 5

### Features
- Core features: 3
- Advanced features: 1
- Fragments: 3
- Adapters: 2
- Workers: 1
- Receivers: 1
- Widgets: 1

---

## ✅ Final Verification

Run through this checklist 30 minutes before submission:

1. [ ] Open project in Android Studio
2. [ ] Sync Gradle (no errors)
3. [ ] Build project (successful)
4. [ ] Run on emulator (works)
5. [ ] Test all features (working)
6. [ ] Check documentation (complete)
7. [ ] Convert REPORT.md to PDF
8. [ ] Take fresh screenshots
9. [ ] Clean project (delete build folders)
10. [ ] Zip project
11. [ ] Verify zip file opens correctly
12. [ ] Upload to submission portal

---

## 🌟 Confidence Boosters

**Remember:**
- You've built a complete, functional app
- All requirements are met
- Code is well-organized and documented
- UI is modern and responsive
- You understand every part of your code

**You've got this! 🚀**

---

## 📱 Device Recommendations

### Emulator Settings
- Device: Pixel 5 or Pixel 6
- API Level: 33 (Android 13)
- RAM: 2048 MB
- Internal Storage: 2048 MB

### Physical Device
- Android 7.0 or higher
- USB Debugging enabled
- Developer options enabled
- Good battery level

---

## ⏱️ Time Management

### If You Have:

**5 Minutes**
- Quick build and run test
- Verify no crashes
- Check one feature

**15 Minutes**
- Test all core features
- Check data persistence
- Verify notifications
- Test widget

**30 Minutes**
- Full testing workflow
- Take screenshots
- Update documentation
- Practice demo

**1 Hour**
- Complete testing
- Fix any issues
- Polish documentation
- Prepare for viva
- Practice explanations

---

**Good luck! You're well-prepared! 🎉**
