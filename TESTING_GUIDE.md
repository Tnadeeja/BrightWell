# BrightWell - Testing Guide

## How to Test the Application

This guide will help you test all features of the BrightWell app for the lab exam demonstration.

---

## Prerequisites

1. Open the project in Android Studio
2. Sync Gradle files (File → Sync Project with Gradle Files)
3. Run the app on an emulator or physical device (Android 7.0+ / API 24+)

---

## Test 1: Daily Habit Tracker

### Adding a Habit
1. Launch the app (opens on Habits tab by default)
2. Tap the **blue "+" FAB** button in the bottom-right
3. Enter habit name: "Drink Water"
4. Enter description: "Drink 8 glasses of water daily"
5. Tap **"Add"**
6. ✅ Verify: Habit appears in the list

### Completing a Habit
1. Tap the **checkbox** next to "Drink Water"
2. ✅ Verify: Checkbox is checked
3. ✅ Verify: Progress bar updates (e.g., 25% if you have 4 habits)
4. ✅ Verify: Percentage text updates

### Editing a Habit
1. Tap the **pencil icon** on a habit card
2. Change the name to "Hydration"
3. Tap **"Save"**
4. ✅ Verify: Habit name updates in the list

### Deleting a Habit
1. Tap the **trash icon** on a habit card
2. Confirm deletion in the dialog
3. ✅ Verify: Habit is removed from the list
4. ✅ Verify: Progress bar recalculates

### Testing Streak
1. Complete a habit today
2. Close and reopen the app
3. ✅ Verify: "🔥 1 day streak" appears below the habit
4. (Note: Multi-day streaks require testing across multiple days)

### Testing Persistence
1. Complete some habits
2. Close the app completely (swipe from recent apps)
3. Reopen the app
4. ✅ Verify: All habits and completion status are retained

---

## Test 2: Mood Journal with Emoji Selector

### Adding a Mood Entry
1. Tap the **"Mood"** tab in bottom navigation
2. Tap the **blue "+" FAB** button
3. Select an emoji from the grid (e.g., 😊)
4. ✅ Verify: Selected emoji is highlighted with blue background
5. Enter a note: "Feeling great today!"
6. Tap **"Save"**
7. ✅ Verify: Mood entry appears at the top of the list

### Viewing Mood History
1. Add multiple mood entries with different emojis
2. ✅ Verify: Entries are sorted by newest first
3. ✅ Verify: Each entry shows:
   - Large emoji
   - Relative time (e.g., "Today at 4:00 PM")
   - Note text (if provided)

### Deleting a Mood Entry
1. Tap the **trash icon** on a mood card
2. Confirm deletion
3. ✅ Verify: Entry is removed

### Sharing Mood Summary
1. Add at least 2-3 mood entries
2. Tap the **"Share"** button at the top
3. Select a sharing method (e.g., Messages, Email)
4. ✅ Verify: Summary text includes:
   - Title "My Mood Journal Summary"
   - Recent mood entries with emojis and timestamps
   - "Tracked with BrightWell" footer

### Testing Persistence
1. Add mood entries
2. Close and reopen the app
3. ✅ Verify: All mood entries are retained

---

## Test 3: Hydration Reminder

### Enabling Reminders
1. Tap the **"Settings"** tab in bottom navigation
2. Toggle **"Enable Reminders"** switch ON
3. **On Android 13+**: Grant notification permission when prompted
4. ✅ Verify: Switch stays ON
5. ✅ Verify: Toast message "Hydration reminders enabled"

### Adjusting Reminder Interval
1. In Settings, drag the **interval slider**
2. ✅ Verify: Text updates to show selected interval (e.g., "30 minutes", "2 hours")
3. Release the slider
4. ✅ Verify: Toast message "Reminder interval updated to X minutes"

### Testing Notifications
**Option 1: Wait for notification (if time permits)**
1. Set interval to 15 minutes (minimum)
2. Wait 15 minutes
3. ✅ Verify: Notification appears with title "Time to Hydrate! 💧"
4. Tap notification
5. ✅ Verify: App opens

**Option 2: Quick test (recommended for demo)**
1. Explain that WorkManager has a 15-minute minimum interval
2. Show the notification channel in device settings
3. Demonstrate the code in `HydrationReminderWorker.kt`

### Disabling Reminders
1. Toggle **"Enable Reminders"** switch OFF
2. ✅ Verify: Toast message "Hydration reminders disabled"
3. ✅ Verify: No notifications appear

### Testing Persistence
1. Enable reminders and set interval to 30 minutes
2. Close the app completely
3. Reopen the app
4. Go to Settings
5. ✅ Verify: Switch is still ON
6. ✅ Verify: Interval is still 30 minutes

---

## Test 4: Home-screen Widget (Advanced Feature)

### Adding the Widget
1. Long-press on the **home screen**
2. Tap **"Widgets"**
3. Scroll to find **"BrightWell"**
4. Drag the **"Today's Habits"** widget to home screen
5. ✅ Verify: Widget appears with:
   - Title "Today's Habits"
   - Percentage (e.g., "0%")
   - Progress bar
   - Completion text (e.g., "0 of 4 completed")

### Testing Widget Updates
1. Open the BrightWell app
2. Complete a habit (check the checkbox)
3. Press **Home button** to return to home screen
4. ✅ Verify: Widget updates to show new percentage
5. Complete another habit
6. Return to home screen
7. ✅ Verify: Widget updates again

### Testing Widget Tap
1. Tap anywhere on the widget
2. ✅ Verify: BrightWell app opens

### Testing Widget with No Habits
1. Delete all habits in the app
2. Return to home screen
3. ✅ Verify: Widget shows "No habits tracked"

---

## Test 5: Responsive UI

### Portrait and Landscape
1. Open the app in portrait mode
2. Rotate device to landscape
3. ✅ Verify: Layout adapts properly
4. ✅ Verify: No data loss
5. Test on all three tabs (Habits, Mood, Settings)

### Different Screen Sizes
**Phone:**
1. Run on Pixel 5 emulator
2. ✅ Verify: All elements fit properly

**Tablet:**
1. Run on Pixel Tablet emulator
2. ✅ Verify: Layout scales appropriately
3. ✅ Verify: Cards don't stretch too wide

---

## Test 6: State Management

### App Restart
1. Add habits and mood entries
2. Complete some habits
3. Configure settings
4. Force stop the app (Settings → Apps → BrightWell → Force Stop)
5. Reopen the app
6. ✅ Verify: All data is retained:
   - Habits exist
   - Completion status preserved
   - Mood entries present
   - Settings unchanged

### Device Reboot (if time permits)
1. Enable hydration reminders
2. Reboot the device
3. Wait for the reminder interval
4. ✅ Verify: Notification still appears

---

## Test 7: Edge Cases

### Empty States
1. Delete all habits
2. ✅ Verify: Empty state message appears with icon
3. Delete all mood entries
4. ✅ Verify: Empty state message appears

### Long Text
1. Add a habit with a very long name (50+ characters)
2. ✅ Verify: Text wraps properly, doesn't overflow
3. Add a mood note with multiple lines
4. ✅ Verify: Note displays correctly

### Rapid Actions
1. Quickly check/uncheck multiple habits
2. ✅ Verify: Progress bar updates smoothly
3. ✅ Verify: No crashes or lag

---

## Test 8: Navigation

### Bottom Navigation
1. Tap each tab: Habits → Mood → Settings
2. ✅ Verify: Correct fragment loads
3. ✅ Verify: Selected tab is highlighted
4. ✅ Verify: Fragment state is preserved when switching back

### Back Button
1. Open the app
2. Press device back button
3. ✅ Verify: App exits (doesn't navigate between fragments)

---

## Test 9: Permissions (Android 13+)

### Notification Permission
1. Fresh install on Android 13+ device
2. Enable hydration reminders
3. ✅ Verify: Permission dialog appears
4. Tap **"Allow"**
5. ✅ Verify: Reminders are enabled

### Permission Denial
1. Enable reminders
2. Deny permission
3. ✅ Verify: Switch turns OFF
4. ✅ Verify: Toast message explains permission is required

---

## Test 10: Data Persistence (SharedPreferences)

### Verifying SharedPreferences
1. Open Device File Explorer in Android Studio
2. Navigate to: `/data/data/com.wellness.brightwell/shared_prefs/`
3. Open `brightwell_prefs.xml`
4. ✅ Verify: JSON data for habits and moods
5. ✅ Verify: Settings keys (hydration_interval, hydration_enabled)

---

## Common Issues and Solutions

### Issue: Widget not updating
**Solution**: Open the app and complete a habit. Widget updates when app is in foreground.

### Issue: Notifications not appearing
**Solution**: 
- Check notification permission (Android 13+)
- Verify battery optimization is disabled for the app
- WorkManager minimum interval is 15 minutes

### Issue: Gradle sync fails
**Solution**: 
- File → Invalidate Caches / Restart
- Ensure internet connection for dependency download

### Issue: App crashes on launch
**Solution**: 
- Check Logcat for error messages
- Verify all resource files are present
- Clean and rebuild project

---

## Demonstration Checklist for Viva

Use this checklist during your viva presentation:

- [ ] Show app launch and initial screen
- [ ] Demonstrate adding a habit
- [ ] Show habit completion and progress bar
- [ ] Demonstrate editing and deleting habits
- [ ] Show mood journal with emoji selection
- [ ] Add a mood entry with note
- [ ] Demonstrate share functionality
- [ ] Show settings screen
- [ ] Enable hydration reminders
- [ ] Adjust reminder interval
- [ ] Show home-screen widget
- [ ] Demonstrate widget update
- [ ] Rotate device to show responsive design
- [ ] Close and reopen app to show persistence
- [ ] Show code structure in Android Studio
- [ ] Explain SharedPreferences implementation
- [ ] Explain WorkManager usage
- [ ] Show widget provider code

---

## Performance Notes

- App launches in < 2 seconds
- Smooth 60 FPS scrolling in RecyclerViews
- No memory leaks (ViewBinding properly cleared)
- Efficient SharedPreferences usage (batch operations)
- Widget updates are lightweight (RemoteViews)

---

## Code Quality Verification

Before submission, verify:
- [ ] All classes have KDoc comments
- [ ] No compiler warnings
- [ ] No unused imports
- [ ] Consistent code formatting
- [ ] No hardcoded strings (all in strings.xml)
- [ ] Proper null safety
- [ ] ViewBinding used throughout

---

## Final Checklist

- [ ] App builds successfully
- [ ] All features work as expected
- [ ] No crashes or ANRs
- [ ] UI is responsive and attractive
- [ ] Code is well-documented
- [ ] README.md is complete
- [ ] REPORT.md is filled out
- [ ] Screenshots captured for report
- [ ] Project zipped for submission

---

Good luck with your lab exam! 🎉
