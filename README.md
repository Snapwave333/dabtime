<div align="center">

# 🔥 Dab Time

### *Your Precision Dabbing Timer Companion*

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://www.android.com/)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org/)
[![Firebase](https://img.shields.io/badge/Backend-Firebase-orange.svg)](https://firebase.google.com/)
[![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-blue.svg)](https://developer.android.com/jetpack/compose)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg)](CONTRIBUTING.md)

**Master the perfect temperature zones • Track your sessions • Compete with friends**

[Features](#-features) • [Tech Stack](#-tech-stack) • [Getting Started](#-getting-started) • [Screenshots](#-screenshots) • [Contributing](#-contributing)

---

</div>

## 🎯 About

**Dab Time** is a modern Android application designed for the dabbing community. Built with precision in mind, it helps users achieve the perfect temperature timing for their sessions through an intelligent heat-zone system, social features, and comprehensive session tracking.

Whether you're a beginner learning the ropes or a seasoned enthusiast perfecting your technique, Dab Time provides the tools you need for a consistent, optimal experience.

## ✨ Features

### 🕐 **Precision Timer**
- **Smart Heat Zones**: Visual indicators for High Temp, Medium Temp, and Low Temp ranges
- **Multiple Modes**: Strict (75s), Balanced (90s), and Chill (120s) timer presets
- **State Persistence**: Timer survives phone rotation and app backgrounding
- **Haptic Feedback**: Feel the heat zone transitions

### 👥 **Social Features**
- **Friend System**: Connect with other dabbing enthusiasts
- **Leaderboards**: Weekly rankings based on precision scores
- **Session Sharing**: Share your sessions with friends (optional)
- **Real-time Notifications**: Get notified when friends are dabbing

### 🏆 **Achievement System**
- **Badges & Milestones**: Unlock achievements for your dedication
- **Precision Scoring**: Track your accuracy over time
- **Session History**: View all your past dabs and statistics

### ⚙️ **Customization**
- **Theme Options**: Light, Dark, and Auto themes
- **Haptic Controls**: Toggle vibration feedback
- **Sound Effects**: Optional audio cues
- **Privacy Options**: Anonymous mode available

### 🔒 **Security & Authentication**
- **Multiple Sign-in Options**: Google, Facebook, Email, or Anonymous
- **ProGuard Enabled**: Secure, obfuscated release builds
- **Firebase Backend**: Industry-standard security and data protection

## 🛠️ Tech Stack

### Core Technologies
- **Language**: Kotlin 1.9.0+
- **UI Framework**: Jetpack Compose (Material 3)
- **Architecture**: MVVM + Repository Pattern
- **Dependency Injection**: Hilt (Dagger)
- **Async Operations**: Kotlin Coroutines & Flow

### Backend & Services
- **Authentication**: Firebase Auth
- **Database**: Cloud Firestore
- **Real-time Database**: Firebase Realtime Database
- **Cloud Functions**: Firebase Functions
- **Analytics**: Firebase Analytics

### Android Components
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Build System**: Gradle 8.0+
- **Navigation**: Jetpack Navigation Compose

## 📱 Screenshots

<div align="center">

| Timer Screen | Social Feed | Leaderboard | Settings |
|:---:|:---:|:---:|:---:|
| *Precision timing with heat zones* | *Connect with friends* | *Weekly rankings* | *Customize your experience* |

> 📸 Screenshots coming soon! The app is currently in active development.

</div>

## 🚀 Getting Started

### Prerequisites

Before you begin, ensure you have the following installed:
- [Android Studio](https://developer.android.com/studio) (Flamingo or later)
- JDK 11 or higher
- Android SDK (API 24+)
- Firebase account (for backend services)

### Firebase Setup

1. **Create a Firebase Project**
   - Go to [Firebase Console](https://console.firebase.google.com/)
   - Create a new project or use an existing one

2. **Add Android App to Firebase**
   - Register your app with package name: `com.dabtime.app`
   - Download `google-services.json`
   - Place it in the `app/` directory

3. **Enable Authentication Methods**
   - Navigate to Authentication > Sign-in method
   - Enable Anonymous, Google, Facebook, and Email/Password

4. **Create Firestore Database**
   - Navigate to Firestore Database
   - Create database in production mode
   - Set up security rules as needed

### Installation

1. **Clone the Repository**
   ```bash
   git clone https://github.com/Snapwave333/dabtime.git
   cd dabtime
   ```

2. **Configure Firebase**
   ```bash
   # Place your google-services.json in app/
   cp /path/to/your/google-services.json app/
   ```

3. **Update Credentials** (Optional - for OAuth)

   Edit `app/src/main/res/values/strings.xml`:
   ```xml
   <!-- Replace with your actual credentials -->
   <string name="default_web_client_id">YOUR_GOOGLE_WEB_CLIENT_ID</string>
   <string name="facebook_app_id">YOUR_FACEBOOK_APP_ID</string>
   <string name="facebook_client_token">YOUR_FACEBOOK_CLIENT_TOKEN</string>
   ```

4. **Build the Project**
   ```bash
   # Debug build
   ./gradlew assembleDebug

   # Release build (requires signing configuration)
   ./gradlew assembleRelease
   ```

5. **Run on Device/Emulator**
   - Open project in Android Studio
   - Connect Android device or start emulator
   - Click Run ▶️ or press Shift+F10

## 📂 Project Structure

```
dabtime/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/dabtime/app/
│   │   │   │   ├── data/              # Data layer
│   │   │   │   │   ├── model/         # Data models
│   │   │   │   │   └── repository/    # Repository implementations
│   │   │   │   ├── di/                # Dependency injection modules
│   │   │   │   ├── ui/                # UI layer
│   │   │   │   │   ├── screens/       # Compose screens
│   │   │   │   │   ├── theme/         # App theming
│   │   │   │   │   └── navigation/    # Navigation logic
│   │   │   │   └── DabTimeApplication.kt
│   │   │   ├── res/                   # Resources (layouts, drawables, etc.)
│   │   │   └── AndroidManifest.xml
│   │   └── test/                      # Unit tests
│   ├── build.gradle                   # App-level build config
│   └── proguard-rules.pro            # ProGuard rules
├── build.gradle                       # Project-level build config
├── IMPROVEMENTS.md                    # Detailed improvement log
└── README.md                          # This file
```

## 💻 Development

### Code Quality

This project follows Android development best practices:

- ✅ MVVM architecture pattern
- ✅ Repository pattern for data access
- ✅ Dependency injection with Hilt
- ✅ Reactive programming with Kotlin Flow
- ✅ Material 3 Design guidelines
- ✅ ProGuard enabled for release builds
- ✅ Comprehensive error handling

### Recent Improvements

Check out [IMPROVEMENTS.md](IMPROVEMENTS.md) for a detailed log of recent enhancements, including:

- ✅ Fixed broken authentication system
- ✅ Implemented Firebase data persistence
- ✅ Added all missing dialog UIs
- ✅ Enhanced security with ProGuard
- ✅ Improved timer performance and state management
- ✅ Better error handling throughout

### Building for Release

```bash
# Generate signed release APK
./gradlew assembleRelease

# Generate signed App Bundle (for Play Store)
./gradlew bundleRelease
```

> **Note**: Release builds require signing configuration in `app/build.gradle`

## 🧪 Testing

```bash
# Run unit tests
./gradlew test

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Generate test coverage report
./gradlew jacocoTestReport
```

> **Note**: Test suite is currently under development. Contributions welcome!

## 🤝 Contributing

We love contributions! Whether it's bug reports, feature requests, or code contributions, all are welcome.

### How to Contribute

1. **Fork the Repository**
   ```bash
   # Click the 'Fork' button on GitHub
   ```

2. **Create a Feature Branch**
   ```bash
   git checkout -b feature/amazing-feature
   ```

3. **Make Your Changes**
   - Write clean, documented code
   - Follow existing code style
   - Add tests if applicable

4. **Commit Your Changes**
   ```bash
   git commit -m "Add amazing feature"
   ```

5. **Push to Your Fork**
   ```bash
   git push origin feature/amazing-feature
   ```

6. **Open a Pull Request**
   - Describe your changes
   - Link any related issues

### Development Guidelines

- Follow [Kotlin coding conventions](https://kotlinlang.org/docs/coding-conventions.html)
- Use meaningful commit messages
- Update documentation as needed
- Ensure all tests pass before submitting PR
- Keep PRs focused on a single feature/fix

## 🗺️ Roadmap

### Version 1.0 (Current)
- [x] Core timer functionality
- [x] Heat zone visualization
- [x] Firebase authentication
- [x] Settings persistence
- [x] State management

### Version 1.1 (Planned)
- [ ] Complete OAuth integration (Google, Facebook)
- [ ] Real-time friend status updates
- [ ] Push notifications
- [ ] Session analytics dashboard
- [ ] Comprehensive testing suite

### Version 2.0 (Future)
- [ ] Customizable heat zone thresholds
- [ ] Timer templates & presets
- [ ] Advanced statistics & graphs
- [ ] Export session data
- [ ] Multi-language support
- [ ] Wear OS companion app

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2024 Dab Time

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software...
```

## 🙏 Acknowledgments

- **Jetpack Compose Team** - For the amazing UI toolkit
- **Firebase Team** - For the robust backend infrastructure
- **Dagger/Hilt Team** - For making DI painless
- **The Dabbing Community** - For inspiration and feedback

## 📧 Contact & Support

- **Issues**: [GitHub Issues](https://github.com/Snapwave333/dabtime/issues)
- **Discussions**: [GitHub Discussions](https://github.com/Snapwave333/dabtime/discussions)
- **Email**: support@dabtime.app

## 🌟 Show Your Support

If you find this project helpful, please consider:

- ⭐ Starring the repository
- 🐛 Reporting bugs
- 💡 Suggesting new features
- 🤝 Contributing code
- 📢 Sharing with others

---

<div align="center">

**Built with ❤️ for the dabbing community**

Made with Kotlin • Powered by Firebase • Designed with Material 3

[⬆ Back to Top](#-dab-time)

</div>
