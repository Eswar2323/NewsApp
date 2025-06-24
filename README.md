# 📰 News App - Android Application

## 📱 Overview

The **News App** is a fully functional Android application that brings real-time news to users with 
powerful features such as favorites management, WebView reading, offline handling, undo deletion, and 
region-specific searching. It integrates modern Android components to deliver a smooth and intuitive experience.

---

## 🚀 Key Features

- 🌐 **Live News Feed**
  - Top headlines from the **US** and some **Indian** sources
  - Retrieved using the News API
  - Displayed in a `RecyclerView`

- 🔍 **Advanced Search**
  - Search bar allows keyword-based queries
  - Not limited to just headlines — searches the entire article body
  - Fetches content from U.S. and Indian news sources

- 📖 **Article View**
  - Opens selected article in `WebView`
  - Floating **Favorite button** to save articles locally

- ⭐ **Favorites System**
  - Save your favorite articles using **Room Database**
  - Prevents duplicate entries with proper user feedback
  - **Swipe left or right** to delete a favorite

- ↩️ **Undo Delete**
  - When an article is deleted from favorites, an **Undo button** allows quick restoration

- 📶 **No Internet Detection**
  - Automatically detects connectivity status
  - Shows a custom message if offline

- 🧭 **Navigation Architecture**
  - Uses Jetpack Navigation Component for smooth transitions

- 🔔 **Popup Feedback**
  - Snackbar/Toast confirms every important action:
    - Added to favorites
    - Removed from favorites
    - Already exists in favorites

---

## 🛠️ Tech Stack

| Component          | Technology                        |
|--------------------|------------------------------------|
| Language            | Kotlin / Java                     |
| API Integration     | Retrofit + GSON                   |
| Local Storage       | Room Database                     |
| UI Components       | RecyclerView, WebView, Snackbar   |
| Architecture        | MVVM (optional)                   |
| Navigation          | Jetpack Navigation Component      |
| Internet Check      | ConnectivityManager               |
| Image Loading       | Glide / Picasso                   |

---

