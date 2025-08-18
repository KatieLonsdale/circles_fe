# ChatterBox (Android App Frontend)
ChatterBox is an Android app for creating groups (friends, family, teams, classes) and sharing posts, links, and photos privately and across multiple groups at once. It’s built with Kotlin and Jetpack Compose, with a focus on simple UX, modern Android architecture, and clear privacy controls.
This repo is the Android frontend. If you’re looking for the backend/API, see: https://github.com/KatieLonsdale/circles_app

## Table of Contents
- [Features](#features)
- [Screenshots](#screenshots)
- [Tech Stack](#tech-stack)
- [Getting Started](#getting-started)
- [License](#license)

## Features
- Create and manage Chatters (private groups)
- Post text, links, and images
- Share to multiple Chatters at once
- Visibility controls to choose who can view and interact with posts
- Notifications of new posts and comments
- Clean, accessible Jetpack Compose UI

## Screenshots

### Sign In Screen
<img src="https://github.com/user-attachments/assets/ac11f82b-741e-47b5-9dde-1ca3a6d8de46" alt="Alt text" width="300" />

### Chatter Newsfeed
<img src="https://github.com/user-attachments/assets/2b23216f-12b5-4ce6-becc-bc2d5aac7290" alt="Alt text" width="300" />

### Display Post
<img src="https://github.com/user-attachments/assets/8455e8d5-733b-4aac-8cce-7269cbf3d2b1" alt="Alt text" width="300" />

### Notifications
<img src="https://github.com/user-attachments/assets/eca41f2e-2abc-4504-acf6-be1d7823f36d" alt="Alt text" width="300" />

## Tech Stack
- Language: Kotlin
- UI: Jetpack Compose, Material 3
- Networking: Retrofit + OkHttp (with logging)
- Image loading: Coil (Compose)
- Push Notifications: Firebase Cloud Messaging

## Getting started
### Prerequisites
- Android Studio Koala+ (or latest stable)
- JDK 17
- Android Gradle Plugin
- Android SDK: compile/target SDK as declared in build.gradle.kts

### Clone
```
git clone https://github.com/KatieLonsdale/circles_fe.git
cd circles_fe
```

### Running the app
From Android Studio: Click Run ▶ (choose a device/emulator)

### Environment & Config

The app expects the [ChatterBox backend API](https://github.com/KatieLonsdale/circles_app). Clone down and run locally.
In build.gradle.app file
```
buildTypes {
        debug {
            // Development URL (emulator localhost)
            buildConfigField("String", "API_BASE_URL", "\"http://10.0.2.2:{your_local_port}/api/v0/\"")
        }...
}
```
## License
License: Proprietary, evaluation-only. See LICENSE.

