# BrightWell - Viva Preparation Guide

## Expected Questions and Answers

### 1. Project Overview Questions

**Q: What is BrightWell and what problem does it solve?**
A: BrightWell is a personal wellness Android application that helps users manage their daily health routines. It solves the problem of tracking healthy habits, monitoring emotional well-being, and maintaining proper hydration throughout the day. The app provides a centralized platform for wellness management with visual progress tracking.

**Q: What are the main features of your app?**
A: The app has four main features:
1. Daily Habit Tracker - Add, edit, delete habits and track daily completion
2. Mood Journal - Log moods with emoji selector and optional notes
3. Hydration Reminder - Configurable notifications to remind users to drink water
4. Home Screen Widget - Shows today's habit completion percentage (advanced feature)

**Q: Why did you choose the widget as your advanced feature?**
A: I chose the home screen widget because:
- It demonstrates advanced Android development skills
- It provides real value to users by showing progress at a glance
- It shows understanding of AppWidgetProvider and RemoteViews
- It's more impressive and practical than basic sensor integration
- It integrates well with the habit tracking feature

---

### 2. Architecture Questions

**Q: What architecture pattern did you use?**
A: I used an MVVM-lite pattern with clear separation of concerns:
- **Data Layer**: Habit and MoodEntry data classes, PreferencesManager
- **UI Layer**: Fragments for each screen, RecyclerView adapters
- **Logic Layer**: Business logic in fragments and utility classes
This keeps the code organized and maintainable.

**Q: Why did you use Fragments instead of Activities?**
A: Fragments provide several advantages:
- Better resource management (single Activity, multiple Fragments)
- Easier navigation with bottom navigation bar
- Better support for tablets and different screen sizes
- Shared ViewModel support if needed
- Follows modern Android development best practices

**Q: Explain your project structure.**
A: The project is organized into logical packages:
- `data/` - Data models and PreferencesManager
- `ui/` - All UI components (fragments and adapters)
- `notifications/` - Background tasks and notification handling
- `widget/` - Home screen widget implementation
- `utils/` - Utility classes like DateUtils
This structure follows separation of concerns and makes the code easy to navigate.

---

### 3. Data Persistence Questions

**Q: How did you implement data persistence?**
A: I used SharedPreferences with Gson for JSON serialization:
- Habits are stored as a JSON array of Habit objects
- Mood entries are stored as a JSON array of MoodEntry objects
- Settings are stored as key-value pairs
- PreferencesManager class centralizes all storage operations

**Q: Why SharedPreferences instead of a database?**
A: As per the lab requirements, we cannot use databases. SharedPreferences is suitable because:
- Simple key-value storage
- Fast read/write operations
- Automatic backup support
- No setup overhead
- Sufficient for the app's data needs
For a production app with more complex data, I would use Room database.

**Q: How do you handle data serialization?**
A: I use Gson library to convert objects to JSON strings:
```kotlin
val json = gson.toJson(habits)
prefs.edit().putString(KEY_HABITS, json).apply()
```
And deserialize back:
```kotlin
val type = object : TypeToken<MutableList<Habit>>() {}.type
return gson.fromJson(json, type)
```

---

### 4. Feature Implementation Questions

**Q: How does the habit tracker work?**
A: The habit tracker:
1. Stores habits in SharedPreferences as JSON
2. Each habit has a Set of completion dates in "yyyy-MM-dd" format
3. RecyclerView displays habits with checkboxes
4. Checking a habit toggles the current date in completedDates
5. Progress card calculates percentage: (completed / total) * 100
6. Widget updates when habits change

**Q: Explain the mood journal implementation.**
A: The mood journal:
1. Shows a dialog with 20 emojis in a GridLayout
2. User selects an emoji (visual feedback with background color)
3. Optional note can be added
4. MoodEntry object created with emoji, note, and timestamp
5. Stored in SharedPreferences
6. Displayed in chronological order (newest first)
7. Can be shared via implicit intent

**Q: How do hydration reminders work?**
A: Hydration reminders use WorkManager:
1. User enables reminders and sets interval in Settings
2. PeriodicWorkRequest is created with the interval
3. HydrationWorker executes periodically
4. Creates notification channel (Android 8.0+)
5. Sends notification with PendingIntent to open app
6. BootReceiver reschedules reminders after device reboot

**Q: How does the widget update?**
A: The widget updates through:
1. HabitWidgetProvider extends AppWidgetProvider
2. Reads habits from SharedPreferences
3. Calculates completion percentage
4. Updates RemoteViews with new data
5. Broadcasts update intent when habits change
6. Widget also updates on its own schedule (every 30 minutes)

---

### 5. Technical Implementation Questions

**Q: What is WorkManager and why did you use it?**
A: WorkManager is Android's recommended solution for deferrable, guaranteed background work. I used it because:
- More reliable than AlarmManager
- Respects battery optimization
- Handles device reboots automatically
- Supports periodic tasks
- Works across all Android versions
- Google's recommended approach

**Q: How do you handle Android 13+ notification permissions?**
A: For Android 13+, I request POST_NOTIFICATIONS permission at runtime:
```kotlin
if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
    if (permission not granted) {
        notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
    }
}
```
Using ActivityResultContracts API for the permission request.

**Q: How did you implement the emoji selector?**
A: The emoji selector:
1. Creates a GridLayout with 5 columns
2. Dynamically adds CardView for each emoji
3. Sets click listener on each card
4. Highlights selected emoji by changing background color
5. Stores selected emoji in a variable
6. Returns emoji when user clicks Save

**Q: Explain your use of ViewBinding.**
A: ViewBinding provides type-safe view access:
```kotlin
private var _binding: FragmentHabitsBinding? = null
private val binding get() = _binding!!

override fun onCreateView(...): View {
    _binding = FragmentHabitsBinding.inflate(inflater, container, false)
    return binding.root
}

override fun onDestroyView() {
    super.onDestroyView()
    _binding = null  // Prevent memory leaks
}
```

---

### 6. UI/UX Questions

**Q: How did you make the UI responsive?**
A: The UI is responsive through:
- ConstraintLayout for flexible layouts
- ScrollView/NestedScrollView for scrollable content
- RecyclerView for efficient list display
- Material Design components that adapt to screen size
- Proper use of match_parent and wrap_content
- Testing on different screen sizes and orientations

**Q: What is Material Design and how did you implement it?**
A: Material Design is Google's design system for Android. I implemented it using:
- Material Components library (Material3)
- MaterialCardView for cards
- MaterialButton for buttons
- FloatingActionButton for primary actions
- Bottom navigation for main navigation
- Proper color scheme and typography
- Elevation and shadows for depth

**Q: How does navigation work in your app?**
A: Navigation uses:
- BottomNavigationView with 3 items (Habits, Mood, Settings)
- Fragment transactions to switch between screens
- Single Activity architecture
- Each navigation item loads a different fragment
- State is preserved during navigation

---

### 7. Code Quality Questions

**Q: How did you ensure code quality?**
A: I ensured code quality through:
- Clear naming conventions (camelCase, descriptive names)
- KDoc comments on all classes and methods
- Logical package structure
- Single responsibility principle
- No code duplication
- Proper null safety with Kotlin
- Resource cleanup in lifecycle methods
- Following Android best practices

**Q: What design patterns did you use?**
A: I used several design patterns:
- **Singleton**: PreferencesManager (single instance)
- **Adapter**: RecyclerView adapters for list display
- **Observer**: Fragment lifecycle callbacks
- **Factory**: Creating notification channels
- **Repository**: PreferencesManager as data source

**Q: How do you handle memory leaks?**
A: I prevent memory leaks by:
- Setting binding to null in onDestroyView
- Using application context for long-lived objects
- Properly managing fragment lifecycle
- Canceling work when not needed
- Not holding references to activities/fragments in background tasks

---

### 8. Testing Questions

**Q: How did you test your app?**
A: I tested through:
- **Functional testing**: All features work as expected
- **UI testing**: Different screen sizes and orientations
- **Data persistence**: App restart, clear data
- **Edge cases**: Empty states, long text, many items
- **Integration**: Widget updates, notifications, sharing
- **Device testing**: Emulator and physical device

**Q: What challenges did you face and how did you solve them?**
A: Main challenges:
1. **Widget updates**: Solved by broadcasting update intent from fragments
2. **Notification permissions**: Handled with runtime permission request
3. **Date-based tracking**: Used Set<String> with date format
4. **Emoji selection feedback**: Changed card background color on selection

---

### 9. Advanced Questions

**Q: How would you improve this app for production?**
A: For production, I would add:
- Room database for better data management
- ViewModel and LiveData for reactive UI
- Dependency injection (Hilt/Dagger)
- Unit and UI tests
- Dark mode support
- Cloud sync with Firebase
- Analytics and crash reporting
- Accessibility improvements
- Localization for multiple languages

**Q: Explain the lifecycle of a Fragment.**
A: Fragment lifecycle:
1. onAttach() - Fragment attached to activity
2. onCreate() - Fragment created
3. onCreateView() - Inflate layout
4. onViewCreated() - View hierarchy created
5. onStart() - Fragment visible
6. onResume() - Fragment interactive
7. onPause() - Fragment losing focus
8. onStop() - Fragment no longer visible
9. onDestroyView() - View hierarchy destroyed
10. onDestroy() - Fragment destroyed
11. onDetach() - Fragment detached from activity

**Q: What is the difference between implicit and explicit intents?**
A: 
- **Explicit Intent**: Specifies exact component to start
  ```kotlin
  Intent(context, MainActivity::class.java)
  ```
- **Implicit Intent**: Declares action, system finds suitable component
  ```kotlin
  Intent(Intent.ACTION_SEND).apply { type = "text/plain" }
  ```
I used explicit intent for opening the app from notifications, and implicit intent for sharing mood summary.

---

### 10. Demonstration Tips

**During the viva, be prepared to:**

1. **Run the app** and demonstrate all features
2. **Add a habit** and show it persists after restart
3. **Log a mood** and share the summary
4. **Enable notifications** and explain the permission request
5. **Show the widget** on home screen
6. **Navigate the code** and explain key classes
7. **Explain design decisions** for each feature
8. **Show responsive design** by rotating device
9. **Handle edge cases** like empty states
10. **Discuss improvements** you would make

---

## Key Points to Remember

### Requirements Met ✅
- ✅ Daily Habit Tracker with CRUD operations
- ✅ Mood Journal with emoji selector
- ✅ Hydration reminders with WorkManager
- ✅ Home screen widget (advanced feature)
- ✅ SharedPreferences for data persistence
- ✅ Implicit/explicit intents
- ✅ State management across sessions
- ✅ Responsive UI for all screen sizes

### Technical Highlights
- Clean architecture with separation of concerns
- Well-documented code with KDoc comments
- Material Design 3 implementation
- Proper lifecycle management
- Efficient RecyclerView usage
- Background task scheduling
- Widget development

### Be Confident About
- Your design decisions
- Code organization
- Feature implementations
- Problem-solving approach
- Understanding of Android concepts

### If You Don't Know Something
- Be honest and say you would research it
- Explain your thought process
- Relate it to what you do know
- Show willingness to learn

---

## Final Checklist Before Viva

- [ ] App builds and runs without errors
- [ ] All features work correctly
- [ ] Code is well-commented
- [ ] README and REPORT are complete
- [ ] Screenshots are taken
- [ ] You understand every line of code
- [ ] You can explain design decisions
- [ ] Device/emulator is ready for demo
- [ ] Project is backed up

**Good luck with your viva! You've built a solid, well-architected app. Be confident! 🌟**
