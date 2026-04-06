<h1 align="center">💰 Expense & Budget Tracker</h1>

<p align="center">
  <i>A robust, offline-first Android application built with Jetpack Compose to seamlessly manage personal finances, track daily transactions, and monitor budget limits. Designed and developed as a comprehensive internship project.</i>
</p>

---

## 📸 UI Previews

| Splash Screen | Login | Signup | Dashboard |
|:---:|:---:|:---:|:---:|
| <img src="Screenshot/Screenshot 2026-04-06 134036.png" width="220" alt="Splash"/> | <img src="Screenshot/Screenshot 2026-04-06 134100.png" width="220" alt="Login"/> | <img src="Screenshot/Screenshot 2026-04-06 134117.png" width="220" alt="SignUp"/> | <img src="Screenshot/Screenshot 2026-04-06 134549.png" width="220" alt="DashBoard"/> |

| Add Transaction | Transaction History | Budget & Analytics | Profile |
|:---:|:---:|:---:|:---:|
| <img src="Screenshot/Screenshot 2026-04-06 134358.png" width="220" alt="Add"/> | <img src="Screenshot/Screenshot 2026-04-06 134817.png" width="220" alt="History"/> | <img src="Screenshot/Screenshot 2026-04-06 134759.png" width="220" alt="Budget"/> | <img src="Screenshot/Screenshot 2026-04-06 134232.png" width="220" alt="Profile"/> |
 
---

## 🚀 Core Features & Technical Highlights

### 1. 🔐 Firebase Integration (Authentication & Cloud Sync)
* **Secure Auth:** User registration and login managed via Firebase Authentication.
* **Data Resilience:** User profiles, custom categories, and transaction histories are synced to Firebase Cloud Firestore/Realtime Database, ensuring seamless cross-device accessibility and preventing data loss.

### 2. 🗄️ Offline-First Architecture (Room Database)
* **Local Caching:** All transactions and budget limits are persisted locally using Room. The app is fully functional without an active internet connection.
* **Reactive UI:** The application relies on a Single Source of Truth. The UI observes the local Room database via Kotlin `Flow`, resulting in zero-latency UI updates while network syncs happen asynchronously in the background.

### 3. ⏰ Smart Notifications (WorkManager)
* **Instant Budget Alerts:** Triggers Android Local Notifications immediately if a new expense pushes the total monthly spending over the user's defined budget limit.
* **Predictive Rate Monitoring:** Utilizes Android `WorkManager` to run daily background health checks. It calculates the current daily spending rate and alerts the user proactively if they are on track to exhaust their budget before the month ends.

### 4. 📊 Data Visualization (Vico Graphs)
* **Interactive Analytics:** Integrates the performant [Vico UI library](https://patrykandpatrick.com/vico) to render smooth, dynamic line charts.
* **Trend Analysis:** Users can filter their spending trajectory by Week, Month, and Year to gain actionable financial insights.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose / Material Design 3
* **Architecture:** MVVM (Model-View-ViewModel)
* **Local Database:** Room Database
* **Dependency Injection:** Hilt / Dagger
* **Backend & Auth:** Firebase
* **Background Processing:** WorkManager
* **Asynchronous Data Streams:** Kotlin Coroutines & Flow

---

## 📱 Screen-by-Screen Breakdown

* **Dashboard:** Central hub displaying Total Balance, Income, Expenses, and a quick-glance list of recent transactions.
* **Add Transaction:** Intuitive form to log Expenses or Income. Includes amount, date picker, and a dynamic category dropdown.
* **Transaction History:** Comprehensive list view with filter tabs (All / Income / Expense). Supports seamless swipe-to-delete functionality which automatically recalculates balances.
* **Analytics & Budget:** Visually tracks spending against a strictly defined monthly limit. Highlights overspent amounts in red and plots data on interactive line graphs.
* **Profile & Settings:** Custom category management system (Add/Delete custom tags like "Freelance" or "Pet Supplies") and secure Firebase logout.

---

## 📂 Project Structure

```text
com.example.expensetracker
│
├── data
│   ├── local         # Room Database, DAOs, Entities
│   ├── remote        # Firebase interactions
│   └── repository    # Single source of truth repository implementation
│
├── di                # Hilt Dependency Injection modules
│
├── domain
│   ├── model         # Domain models
│   └── usecase       # Business logic (e.g., CalculateBudgetStatusUseCase)
│
├── ui
│   ├── components    # Reusable Compose widgets (Buttons, Cards, Graph)
│   ├── screens       # Main UI screens (Dashboard, Analytics, Profile)
│   └── theme         # Typography, Colors, and Shapes
│
└── workers           # WorkManager classes for background notifications
```

---

## ⚙️ Installation
Clone the repository to your local machine:

Bash
git clone [https://github.com/your-username/expense-tracker.git](https://github.com/your-username/expense-tracker.git)
Open the project in Android Studio.

Ensure you have added your google-services.json file to the app/ directory to connect to your Firebase instance.

Sync Gradle to download all dependencies.

Build and run the application on an emulator or physical device.

---

## 👩‍💻 Author
Priyanshi Vishwakarma. 
