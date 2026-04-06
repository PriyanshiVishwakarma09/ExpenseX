<h1 align="center">💰 ExpenseX</h1>

<p align="center">
  <i>A robust, offline-first Android application built with Jetpack Compose to seamlessly manage personal finances, track daily transactions, and monitor budget limits. Designed and developed as a comprehensive internship project focusing on a clean, product-first mobile experience.</i>
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

### 1. 🎯 Smart Budget Tracker (Goal / Challenge Feature)
* **Monthly Limit Challenge:** To make financial tracking engaging, users can set a strict monthly spending goal. The app dynamically tracks progress, acting as a "no-spend" awareness challenge.
* **Instant Alerts (WorkManager):** Triggers Android Local Notifications immediately if a new expense pushes the total spending over the defined budget limit.

### 2. 🗄️ Offline-First Architecture (Data Handling)
* **Local Storage (Room SQLite):** All transactions and limits are persisted locally for a lightning-fast, zero-latency experience that works perfectly in airplane mode.
* **Cloud Sync (Firebase):** In the background, data is securely synced to Firebase Cloud Firestore and Authentication, ensuring seamless cross-device accessibility and data recovery.

### 3. 📊 Insights & Data Visualization (Vico Graphs)
* **Interactive Analytics:** Integrates the performant [Vico UI library](https://patrykandpatrick.com/vico) to transform raw data into a smooth, interactive line chart.
* **Trend Analysis:** Users can filter their spending trajectory by Week, Month, and Year to gain actionable financial insights and understand their spending patterns on a small screen.

### 4. 📱 Smooth Mobile UX & Responsiveness
* **Product Thinking:** Designed specifically for mobile with thumb-friendly navigation, clean form design, and clear visual hierarchies (Green for income, Red for expenses).
* **State Management:** Handles loading states, error handling in forms, and graceful empty states when no transactions exist. UI observes the database via Kotlin `Flow` for reactive updates.

---

## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin
* **UI Framework:** Jetpack Compose / Material Design 3
* **Architecture:** MVVM (Model-View-ViewModel) + Clean Architecture
* **Local Database:** Room Database
* **Dependency Injection:** Hilt / Dagger
* **Backend & Auth:** Firebase
* **Background Processing:** WorkManager
* **Asynchronous Data Streams:** Kotlin Coroutines & Flow

---

## 📱 Screen-by-Screen Breakdown

* **Dashboard:** Central hub displaying a glanceable summary: Total Balance, Income, Expenses, and a quick list of recent transactions.
* **Add Transaction:** Intuitive data entry flow to log Expenses or Income. Includes amount, date picker, notes, and a dynamic category dropdown.
* **Transaction History:** Comprehensive CRUD (Create, Read, Update, Delete) management. Includes categorized filter tabs and seamless swipe-to-delete functionality.
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
