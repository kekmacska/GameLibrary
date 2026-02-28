# 🎮 Game Library — Android App

A fully open‑source game library application for viewing information about computer games and potentional collectibles in them, for Android, built with native Kotlin, Jetpack Compose, and Material 3 Monet dynamic theming. 

---

## ⚙️ Tech Stack

### **Core Technologies**
- **Kotlin**
- **Jetpack Compose** (UI)
- **Material 3** with dynamic color (Monet)
- **Backend:** The original backend uses Laravel 12
- **Database:** The original database handled by the backend is MariaDB

### **Libraries Used**
- **Navigation:** AndroidX Navigation Compose  
- **Image Loading:** Coil 3 (Compose + OkHttp integration)  
- **Networking:** Ktor Client  
- **JSON Serialization:** KotlinX Serialization (via Ktor)  
- **Icons:** Material Icons  
- **Environment variables:** `local.properties`  
  - Not committed to version control  
  - [See example](./local.properties.example)
  - To achive a successful build, you must provide your backend API URL and the location of Android SDK on your system