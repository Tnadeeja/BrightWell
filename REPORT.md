# BrightWell - Lab Exam 03 Report

## Student Information
**Name:** [Your Name]  
**Student ID:** [Your ID]  
**Course:** Mobile App Development  
**Date:** September 30, 2025

---

## 1. Application Overview

BrightWell is a comprehensive personal wellness Android application designed to help users manage their daily health routines. The app combines habit tracking, mood journaling, and hydration reminders into a single, user-friendly platform.

### Purpose
The application promotes personal wellness by:
- Encouraging consistent healthy habits
- Tracking emotional well-being
- Maintaining proper hydration throughout the day
- Providing visual feedback on progress

---

## 2. Features Implementation

### 2.1 Daily Habit Tracker (Required Feature)

**Implementation Details:**
- Users can add unlimited wellness habits (e.g., "Drink Water", "Meditate", "Exercise")
- Each habit includes a name and optional description
- Habits are displayed in a RecyclerView with Material Design cards
- Daily completion tracked via checkboxes
- Progress card shows percentage of habits completed today
- Edit and delete functionality for each habit

**Technical Approach:**
- Data stored in SharedPreferences as JSON using Gson
- `Habit` data class with unique ID, name, description, and completion dates
- `PreferencesManager` handles all CRUD operations
- `HabitsAdapter` efficiently displays habits in RecyclerView
- Date-based completion tracking using "yyyy-MM-dd" format

**Code Highlights:**
```kotlin
data class Habit(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val description: String = "",
    val createdAt: Long = System.currentTimeMillis(),
    val completedDates: MutableSet<String> = mutableSetOf()
)
```

---

### 2.2 Mood Journal with Emoji Selector (Required Feature)

**Implementation Details:**
- 20 different emojis representing various moods
- Grid layout emoji selector in dialog
- Optional text note for each mood entry
- Chronological list of past mood entries
- Each entry shows emoji, date, time, and note
- Share functionality using implicit intents

**Technical Approach:**
- `MoodEntry` data class with emoji, note, and timestamp
- Custom dialog with GridLayout for emoji selection
- Visual feedback for selected emoji (background color change)
- Implicit intent for sharing mood summary
- Formatted date/time display using SimpleDateFormat

**Code Highlights:**
```kotlin
data class MoodEntry(
    val id: String = UUID.randomUUID().toString(),
    val emoji: String,
    val note: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
```

---

### 2.3 Hydration Reminder (Required Feature)

**Implementation Details:**
- Configurable reminder intervals (15 min, 30 min, 1-4 hours)
- Background notifications using WorkManager
- Enable/disable toggle in settings
- Notifications persist after device reboot
- Tapping notification opens the app

**Technical Approach:**
- `HydrationWorker` extends Worker for background execution
- `NotificationScheduler` manages WorkManager scheduling
- `BootReceiver` reschedules notifications after reboot
- Notification channel for Android 8.0+ compatibility
- PendingIntent for app navigation

**Code Highlights:**
```kotlin
class HydrationWorker(context: Context, params: WorkerParameters) : Worker(context, params) {
    override fun doWork(): Result {
        createNotificationChannel()
        sendHydrationNotification()
        return Result.success()
    }
}
```

---

### 2.4 Home Screen Widget (Advanced Feature)

**Implementation Details:**
- Shows today's habit completion percentage
- Displays completed vs total habits count
- Dynamic emoji based on progress (😴 0%, 🎉 100%)
- Gradient background with Material Design
- Updates when habits are checked/unchecked
- Tappable to launch the app

**Technical Approach:**
- `HabitWidgetProvider` extends AppWidgetProvider
- RemoteViews for widget UI updates
- Calculates completion percentage from SharedPreferences
- Broadcast receiver for widget updates
- XML configuration for widget metadata

**Why This Advanced Feature:**
- Demonstrates advanced Android development skills
- Provides real value to users (at-a-glance progress)
- Shows understanding of app widgets and RemoteViews
- More impressive than basic sensor integration

---

## 3. Technical Architecture

### 3.1 Project Structure
```
com.wellness.brightwell/
├── data/                    # Data models and storage
├── ui/                      # UI components (Fragments, Adapters)
├── notifications/           # Background tasks and notifications
├── widget/                  # Home screen widget
├── utils/                   # Utility classes
└── MainActivity.kt          # Main entry point
```

### 3.2 Design Patterns
- **MVVM-lite**: Separation of data, UI, and logic
- **Repository Pattern**: PreferencesManager as single source of truth
- **Adapter Pattern**: RecyclerView adapters for list display
- **Observer Pattern**: Fragment lifecycle management

### 3.3 Data Persistence Strategy
All data stored in SharedPreferences with JSON serialization:
- **Habits**: List of Habit objects
- **Mood Entries**: List of MoodEntry objects  
- **Settings**: Key-value pairs (intervals, enabled states)

**Advantages:**
- No database setup required
- Fast read/write operations
- Automatic backup support
- Simple implementation

---

## 4. User Interface Design

### 4.1 Design Principles
- **Material Design 3**: Modern, consistent UI components
- **Responsive Layout**: Adapts to phones and tablets
- **Intuitive Navigation**: Bottom navigation for easy access
- **Visual Feedback**: Progress bars, cards, and animations
- **Accessibility**: Proper content descriptions and touch targets

### 4.2 Screen Layouts
1. **Habits Screen**: Progress card + RecyclerView list
2. **Mood Screen**: Summary card + mood entries list
3. **Settings Screen**: Grouped settings with cards
4. **Dialogs**: Material Design dialogs for input

### 4.3 Color Scheme
- Primary: Purple (#6200EE)
- Secondary: Teal (#03DAC6)
- Background: Light gray (#F5F5F5)
- Surface: White (#FFFFFF)

---

## 5. Key Technical Decisions

### 5.1 Why WorkManager?
- More reliable than AlarmManager
- Respects system battery optimization
- Handles device reboots automatically
- Recommended by Google for background tasks

### 5.2 Why Gson for JSON?
- Simple API for serialization/deserialization
- Handles complex objects automatically
- Type-safe with TypeToken
- Widely used and well-documented

### 5.3 Why ViewBinding?
- Type-safe view access (no findViewById)
- Null-safe (views are nullable in binding)
- Compile-time verification
- Better performance than Data Binding

---

## 6. Challenges and Solutions

### Challenge 1: Widget Updates
**Problem:** Widget not updating when habits change  
**Solution:** Broadcast intent to trigger widget update from HabitsFragment

### Challenge 2: Notification Permissions (Android 13+)
**Problem:** Runtime permission required for notifications  
**Solution:** Request permission using ActivityResultContracts API

### Challenge 3: Date-based Habit Tracking
**Problem:** Tracking which dates habits were completed  
**Solution:** Store completion dates as Set<String> in "yyyy-MM-dd" format

---

## 7. Testing Performed

### Functional Testing
✅ Add, edit, delete habits  
✅ Check/uncheck habits and verify progress  
✅ Log mood entries with emojis and notes  
✅ Share mood summary via different apps  
✅ Enable/disable hydration reminders  
✅ Change reminder intervals  
✅ Receive notifications at correct intervals  
✅ Widget displays correct progress  
✅ Widget updates when habits change  

### UI Testing
✅ Portrait and landscape orientations  
✅ Different screen sizes (phone, tablet)  
✅ Navigation between fragments  
✅ Dialog interactions  
✅ Button states and feedback  

### Data Persistence Testing
✅ Data survives app restart  
✅ Settings persist across sessions  
✅ Clear data functionality works  

---

## 8. Code Quality Measures

### Documentation
- KDoc comments on all classes and methods
- Inline comments for complex logic
- README with setup instructions
- This comprehensive report

### Organization
- Logical package structure
- Single responsibility principle
- No code duplication
- Consistent naming conventions

### Best Practices
- Proper null safety with Kotlin
- Resource cleanup in onDestroyView
- Efficient RecyclerView with ViewHolder pattern
- Proper lifecycle management
- Material Design guidelines followed

---

## 9. Future Enhancements

If this were a production app, potential improvements:
1. **Database Integration**: Room for better data management
2. **Charts**: MPAndroidChart for mood/habit trends
3. **Reminders**: Custom reminders for individual habits
4. **Themes**: Dark mode support
5. **Export**: Export data to CSV/JSON
6. **Cloud Sync**: Firebase for multi-device sync
7. **Gamification**: Streaks, achievements, rewards

---

## 10. Conclusion

BrightWell successfully implements all required features for Lab Exam 03:
- ✅ Daily Habit Tracker with full CRUD operations
- ✅ Mood Journal with emoji selector and sharing
- ✅ Hydration Reminders with WorkManager
- ✅ Home Screen Widget as advanced feature
- ✅ SharedPreferences for data persistence
- ✅ Responsive UI with Material Design
- ✅ Well-documented, organized code

The application demonstrates proficiency in:
- Android app architecture
- Fragment-based navigation
- Background task scheduling
- Data persistence techniques
- Material Design implementation
- Widget development

**Total Development Time:** Approximately 8-10 hours

---

## 11. Screenshots

### Main Screens
1. **Habits Screen** - Shows progress card and habit list
2. **Mood Journal** - Displays mood entries with emojis
3. **Settings Screen** - Hydration reminder configuration
4. **Add Habit Dialog** - Material Design input dialog
5. **Emoji Selector** - Grid of mood emojis
6. **Home Screen Widget** - Progress display on home screen
7. **Notification** - Hydration reminder notification

*(Screenshots should be added here during presentation)*

---

## 12. References

- Android Developers Documentation: https://developer.android.com
- Material Design Guidelines: https://material.io/design
- WorkManager Guide: https://developer.android.com/topic/libraries/architecture/workmanager
- App Widgets Overview: https://developer.android.com/guide/topics/appwidgets

---

**Declaration:**  
I hereby declare that this project is my original work and has been completed without plagiarism. All resources and references used have been appropriately cited.

**Signature:** _______________  
**Date:** September 30, 2025
