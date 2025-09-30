# BrightWell - Lab Exam 03 Report

**Student Name:** [Your Name]  
**Student ID:** [Your ID]  
**Course:** Mobile Application Development  
**Date:** October 1, 2025

---

## 1. Application Overview

BrightWell is a comprehensive personal wellness Android application designed to help users manage their daily health routines. The app combines habit tracking, mood journaling, and hydration reminders into a single, intuitive interface.

### Key Features
- **Daily Habit Tracker**: Manage and track wellness habits with completion progress
- **Mood Journal**: Log emotions with emoji selection and optional notes
- **Hydration Reminders**: Customizable notifications to stay hydrated
- **Home-screen Widget**: Quick view of today's habit completion percentage

---

## 2. Technical Implementation

### Architecture
The app follows a clean architecture pattern with clear separation of concerns:

```
com.wellness.brightwell/
├── data/                    # Data models and persistence
│   ├── Habit.kt
│   ├── MoodEntry.kt
│   └── PreferencesManager.kt
├── ui/                      # User interface components
│   ├── habits/
│   ├── mood/
│   └── settings/
├── worker/                  # Background tasks
│   ├── HydrationReminderWorker.kt
│   └── BootReceiver.kt
├── widget/                  # Home screen widget
│   └── HabitWidgetProvider.kt
├── utils/                   # Utility classes
│   ├── DateUtils.kt
│   └── NotificationScheduler.kt
└── MainActivity.kt          # Main entry point
```

### Data Persistence
All user data is stored using **SharedPreferences** as required:
- Habits are serialized to JSON using Gson
- Mood entries are stored as JSON arrays
- User settings (reminder intervals, enabled state) use key-value pairs
- Data persists across app sessions and device reboots

### Technologies Used
- **Language**: Kotlin
- **UI Framework**: Material Design 3
- **Architecture Components**: Fragments, ViewBinding, LiveData
- **Background Tasks**: WorkManager for reliable notifications
- **Data Serialization**: Gson for JSON handling
- **Layout**: ConstraintLayout for responsive design

---

## 3. Core Features Implementation

### 3.1 Daily Habit Tracker (1 Mark)

**Implementation Details:**
- Users can add habits with name and description
- Edit and delete functionality with confirmation dialogs
- Daily completion tracking with checkboxes
- Real-time progress bar showing completion percentage
- Streak counter showing consecutive completion days
- Data stored in SharedPreferences using Gson

**Key Classes:**
- `HabitsFragment.kt`: Main UI controller
- `HabitAdapter.kt`: RecyclerView adapter
- `Habit.kt`: Data model with completion tracking

**Screenshots:** [See Appendix A]

---

### 3.2 Mood Journal with Emoji Selector (1 Mark)

**Implementation Details:**
- Grid of 40+ emojis for mood selection
- Optional text notes for each entry
- Timestamp automatically recorded
- Chronological list view of past moods
- Share functionality using implicit intents (ACTION_SEND)
- Delete entries with confirmation

**Key Classes:**
- `MoodFragment.kt`: Main UI controller
- `MoodAdapter.kt`: RecyclerView adapter for mood history
- `EmojiAdapter.kt`: Grid adapter for emoji selection
- `MoodEntry.kt`: Data model with timestamp

**Screenshots:** [See Appendix B]

---

### 3.3 Hydration Reminder (1 Mark)

**Implementation Details:**
- WorkManager for reliable background notifications
- Customizable intervals (15 min to 6 hours)
- Enable/disable toggle in Settings
- Notification channel for Android 8.0+
- Permission handling for Android 13+
- Persists across device reboots using BroadcastReceiver

**Key Classes:**
- `HydrationReminderWorker.kt`: WorkManager worker
- `NotificationScheduler.kt`: Scheduling helper
- `BootReceiver.kt`: Reboot handling
- `SettingsFragment.kt`: Configuration UI

**Screenshots:** [See Appendix C]

---

## 4. Advanced Feature: Home-screen Widget (2 Marks)

**Implementation Details:**
- Displays today's habit completion percentage
- Shows completed/total habit count
- Updates automatically when habits are completed
- Tap to open the app (PendingIntent)
- Responsive design adapts to widget size
- Uses RemoteViews for efficient updates

**Key Classes:**
- `HabitWidgetProvider.kt`: AppWidget provider
- Widget layout: `widget_habit.xml`
- Widget configuration: `habit_widget_info.xml`

**Technical Highlights:**
- Efficient update mechanism using static methods
- Proper PendingIntent flags for Android 12+
- Material Design styling consistent with app

**Screenshots:** [See Appendix D]

---

## 5. Additional Technical Requirements

### 5.1 Fragments and Activities
- **MainActivity**: Hosts bottom navigation and fragment container
- **HabitsFragment**: Habit tracking screen
- **MoodFragment**: Mood journal screen
- **SettingsFragment**: App configuration screen

### 5.2 Intents
**Explicit Intents:**
- Opening MainActivity from widget
- Fragment navigation

**Implicit Intents:**
- Sharing mood summary (ACTION_SEND with text/plain)

### 5.3 State Management
- All settings persist using SharedPreferences
- Habit completion data retained across sessions
- Mood entries saved permanently
- Hydration reminder settings survive app restarts

### 5.4 Responsive UI
- ConstraintLayout for flexible layouts
- Supports portrait and landscape orientations
- Adapts to phones and tablets
- Material Design 3 components scale appropriately

---

## 6. User Interface Design

### Design Principles
- **Clean and Modern**: Material Design 3 guidelines
- **Intuitive Navigation**: Bottom navigation bar with clear icons
- **Visual Feedback**: Progress bars, checkboxes, and animations
- **Accessibility**: Proper content descriptions and touch targets
- **Consistency**: Unified color scheme and typography

### Color Scheme
- **Primary**: Indigo (#6366F1)
- **Secondary**: Green (#10B981)
- **Accent**: Amber (#F59E0B)
- **Background**: Light gray (#F9FAFB)

### Typography
- **Headings**: Bold, 18-24sp
- **Body**: Regular, 14-16sp
- **Captions**: 12-14sp

---

## 7. Code Quality

### Organization
- Clear package structure by feature
- Consistent Kotlin naming conventions
- Proper use of data classes and sealed classes
- No redundant or duplicate code

### Documentation
- KDoc comments on all public classes and functions
- Inline comments explaining complex logic
- README.md with comprehensive project documentation
- This report documenting implementation details

### Best Practices
- Null safety with Kotlin
- ViewBinding for type-safe view access
- Proper lifecycle management in fragments
- Memory leak prevention (clearing binding references in onDestroyView)
- Coroutines-ready architecture (though not required)

---

## 8. Testing and Validation

### Manual Testing Performed
✅ Add, edit, delete habits  
✅ Mark habits as complete/incomplete  
✅ Progress bar updates correctly  
✅ Streak calculation works  
✅ Add mood entries with emojis  
✅ Share mood summary  
✅ Enable/disable hydration reminders  
✅ Adjust reminder intervals  
✅ Notifications appear on schedule  
✅ Widget displays correct data  
✅ Widget updates when habits change  
✅ App survives configuration changes (rotation)  
✅ Data persists across app restarts  

### Device Testing
- **Emulator**: Pixel 5 API 34 (Android 14)
- **Orientations**: Portrait and Landscape
- **Screen Sizes**: Phone and Tablet layouts

---

## 9. Challenges and Solutions

### Challenge 1: WorkManager Minimum Interval
**Problem**: WorkManager has a minimum interval of 15 minutes for periodic work.  
**Solution**: Documented this limitation and set 15 minutes as the minimum selectable interval.

### Challenge 2: Widget Updates
**Problem**: Widgets don't auto-update when data changes.  
**Solution**: Implemented a static update method called whenever habits are modified.

### Challenge 3: Android 13+ Notification Permissions
**Problem**: Runtime permission required for notifications on Android 13+.  
**Solution**: Implemented proper permission request flow using ActivityResultContracts.

---

## 10. Future Enhancements

While not required for this lab exam, potential improvements include:
- Dark theme support
- Habit statistics and charts using MPAndroidChart
- Export/import data functionality
- Cloud sync with Firebase
- Reminder customization (specific times)
- Habit categories and icons
- Achievement system and gamification

---

## 11. Conclusion

BrightWell successfully implements all required features for Lab Exam 03:
- ✅ Daily Habit Tracker with full CRUD operations
- ✅ Mood Journal with emoji selector
- ✅ Hydration Reminder using WorkManager
- ✅ Advanced Feature: Home-screen Widget
- ✅ SharedPreferences for data persistence
- ✅ Responsive UI for all screen sizes
- ✅ Clean, well-documented code

The app demonstrates proficiency in Android development concepts including:
- Fragment-based architecture
- Data persistence without databases
- Background task scheduling
- Widget development
- Material Design implementation
- Intent handling (explicit and implicit)

---

## Appendix A: Habit Tracker Screenshots

[Insert screenshots here showing:]
1. Habits list with progress bar
2. Add habit dialog
3. Edit habit dialog
4. Completed habits with checkmarks
5. Streak display

---

## Appendix B: Mood Journal Screenshots

[Insert screenshots here showing:]
1. Mood list view
2. Add mood dialog with emoji grid
3. Mood entry with note
4. Share mood summary

---

## Appendix C: Hydration Reminder Screenshots

[Insert screenshots here showing:]
1. Settings screen
2. Reminder interval selector
3. Notification example
4. Permission request dialog (Android 13+)

---

## Appendix D: Widget Screenshots

[Insert screenshots here showing:]
1. Widget on home screen
2. Widget with 0% completion
3. Widget with partial completion
4. Widget with 100% completion

---

## Declaration

I declare that this is my original work and has been completed in accordance with the academic integrity policies of the institution. All external resources and references have been properly cited.

**Signature:** ___________________  
**Date:** October 1, 2025
