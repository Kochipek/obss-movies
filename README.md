# 🎬 Movie App

<img width="1942" height="800" alt="image" src="https://github.com/user-attachments/assets/af61487c-8e09-4cc1-b058-a718ac63d2f6" />

This application allows users to explore, search, and manage their personal movie library. It supports guest access, Google login, and user accounts via Firebase Authentication. Users can add movies to their "Watch Later" list, track watched movies, and share films with friends. The app also provides a smooth browsing experience with pagination and an intuitive UI.

## 🏆 Features
- **Guest Login**: Users can explore the app without creating an account.
- **Google Login**: Quick and secure sign-in with Google using Firebase Authentication.
- **User Login**: Email and password-based authentication.
- **Watch Later**: Add movies to a list for future viewing.
- **Watched Movies**: Keep track of the movies you've already seen.
- **Movie Search**: Search for movies by title, genre, or keyword.
- **User-based Movie Archiving**: Personalized movie collection per account.
- **Movie Sharing**: Share movie details with friends via external apps.
- **Paging Support**: Smooth infinite scrolling for large movie collections.

## 👩🏻‍💻 Tech Stack

- [Kotlin](https://kotlinlang.org/) – The modern programming language for Android development.
- [Firebase Auth](https://firebase.google.com/docs/auth) – For user authentication with Email/Password and Google Sign-In.
- [Retrofit](https://github.com/square/retrofit) – HTTP client for making API requests to fetch movie data.
- [Coroutines](https://developer.android.com/kotlin/coroutines) – For managing asynchronous operations.
- [Flow](https://developer.android.com/kotlin/flow) – Reactive streams for data handling.
- [Coil](https://coil-kt.github.io/coil/) – Efficient image loading and caching.
- [Room](https://developer.android.com/training/data-storage/room) – Local database for saving user data and movie lists.
- [Hilt](https://developer.android.com/training/dependency-injection/hilt-android) – Dependency injection for a modular architecture.
- [ViewPager](https://developer.android.com/reference/androidx/viewpager/widget/ViewPager) – For swiping between different movie sections.
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) – Efficiently loads large datasets with pagination.
- [ViewBinding](https://developer.android.com/topic/libraries/view-binding) – Safer and easier view access.
- [Jetpack Navigation](https://developer.android.com/guide/navigation) – For navigation between screens and passing data.
- [DiffUtil](https://developer.android.com/reference/androidx/recyclerview/widget/DiffUtil) – Efficient list updates in RecyclerViews.

## 🗂 Project Structure
- **Single State Management** – Centralized UI state per screen for predictable behavior.
- **Core-Feature Based Modules** – Separation of shared core components and feature-specific modules for scalability.

## 🔒 Security
This application incorporates multiple security measures:
- **Storage of API Keys**: Secret keys are stored securely in `gradle.properties` and not committed to version control.
- **Proguard & R8**: Code obfuscation and optimization to protect sensitive data.
- **Firebase Security Rules**: Restrict access to user-specific data in the database.
