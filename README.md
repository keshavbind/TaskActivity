# TaskActivity — Premium Task Manager

TaskActivity is a modern, feature-rich Android task management application built using clean architecture (MVVM) and Android Jetpack libraries. It features a premium, customized dark theme, sleek card layouts, animations, and local SQLite database persistence.

---

## 🎨 Features & UI Overview

- **Premium Dark-Theme Design:** Designed with a deep slate background, glassmorphism-inspired card borders, and electric indigo gradients.
- **Visual Priority Indicators:** Task cards feature left vertical accent strips representing priority severity:
  - 🔴 **High:** Red
  - 🟡 **Medium:** Yellow
  - 🟢 **Low:** Green
- **Reactive Task Counting:** The header card displays greeting text along with live task counts that dynamically adapt as you add or remove items.
- **Empty State Illustration:** Displays a clean clipboard check icon when the database is empty to prompt user engagement.
- **Completed Task States:** Toggling a task as complete automatically triggers a strikethrough effect and faded opacity for visual prioritization.
- **Input Validation:** Clean text field feedback (TextInputLayout helpers) to ensure titles and descriptions are provided.
- **Fluid Screen Transitions:** Activity enter/exit transitions slide horizontally for a native premium feel.

---

## 🏗️ Architecture & Stack

The project adheres to the **Model-View-ViewModel (MVVM)** architecture pattern:

```
┌─────────────────────────────────────────────────────┐
│                    ACTIVITIES (View)                 │
│  MainActivity ←→ AddTaskActivity / EditTaskActivity  │
│      observes LiveData, handles user interaction     │
└──────────────────────┬──────────────────────────────┘
                       │
              ┌────────▼────────┐
              │   TaskViewModel  │  ← Launches coroutines
              │ + TaskViewModelFactory │
              └────────┬────────┘
                       │
              ┌────────▼────────┐
              │  TaskRepository  │  ← Clean data handling layer
              └────────┬────────┘
                       │
              ┌────────▼────────┐
              │    TaskDao       │  ← Room Data Access Object
              └────────┬────────┘
                       │
              ┌────────▼────────┐
              │  TaskDatabase    │  ← SQLite via Room
              │  (table: tasks)  │
              └─────────────────┘
```

- **Kotlin:** The programming language used.
- **Room Database:** Clean SQLite abstraction layer for persistent storage.
- **Coroutines:** Asynchronous, non-blocking operations for database transactions.
- **LiveData:** Reactive observation of data changes from DB -> ViewModel -> UI.
- **ViewModel:** Lifecycle-aware UI state controller.
- **Material Design 3:** Modern, accessible UI styling framework.

---

## 📂 Project Structure

```
com.example.taskactivity/
├── activitties/
│   ├── MainActivity.kt        # Main task list screen
│   ├── AddTaskActivity.kt     # Form screen for creating tasks
│   └── EditTaskActivity.kt    # Form screen for editing tasks
├── adapter/
│   └── MyAdapter.kt           # Custom task card adapter
├── database/
│   ├── Task.kt                # Database table entity model
│   ├── TaskDao.kt             # Room queries interface
│   └── TaskDatabase.kt        # Database instantiation singleton
├── repository/
│   └── TaskRepository.kt      # Abstraction for database sources
└── viewmodel/
    ├── TaskViewModel.kt       # Communicates repository with activities
    └── TaskViewModelFactory.kt# Dependency instantiation helper
```

---

## ⚙️ Building and Running

1. **Prerequisites:**
   - Android Studio (Koala or newer recommended).
   - Android SDK 35 (minSdk 24, targetSdk 35).
   - JDK 11+.

2. **Setup:**
   - Clone or open this repository folder in Android Studio.
   - Wait for Android Studio to index files and perform a **Gradle Sync**.

3. **Execution:**
   - Connect an Android device or launch an emulator.
   - Click the **Run** button (green arrow icon) in Android Studio to build and deploy.
