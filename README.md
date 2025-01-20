# 🏈 Football Collision

[![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://www.android.com)
[![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Google Maps](https://img.shields.io/badge/Google_Maps-4285F4?style=for-the-badge&logo=google-maps&logoColor=white)](https://developers.google.com/maps)

Football Collision is an exciting Android game that combines quick reflexes, strategic movement, and location-based features to create an engaging mobile gaming experience. Navigate your player through a challenging five-lane road, avoid obstacles, collect power-ups, and compete for the highest score on the global leaderboard!

## 🎮 Features

### Multiple Game Modes
- **Button Mode - Slow**: Perfect for beginners to learn the game mechanics
- **Button Mode - Fast**: Increased difficulty for experienced players
- **Sensor Mode**: Innovative tilt controls for an immersive experience
    - Tilt left/right to move
    - Tilt forward/backward to control speed

### Gameplay Elements
- 🛣️ Five-lane road system for strategic navigation
- 📏 Distance-based scoring system with odometer
- ❤️ Life system with collectable power-ups
- 🔊 Immersive sound effects
- 🏆 Global leaderboard with location tracking
- 🗺️ Google Maps integration to view score locations

## 🎯 How to Play

1. Choose your preferred game mode from the main menu
2. Navigate your player between lanes to avoid obstacles
3. Collect power-ups to gain extra lives
4. Try to travel as far as possible
5. Submit your score to compete on the leaderboard
6. View score locations on the integrated map

## 🛠️ Technical Implementation

### Core Components
- Custom game engine with efficient collision detection
- Sensor-based controls using device accelerometer
- Persistent score storage using SharedPreferences
- Google Maps API integration for location features

### Architecture
- Modern Android development practices
- MVVM architecture
- Fragment-based UI components
- Custom view implementations
- Location services integration


## 🔧 Installation

1. Clone the repository:
```bash
git clone https://github.com/yourusername/football-collision.git
```

2. Open the project in Android Studio

3. Add your Google Maps API key in `AndroidManifest.xml`:
```xml
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="YOUR_API_KEY" />
```

4. Build and run the project on your Android device or emulator

## 🔑 Requirements

- Android SDK 21 or higher
- Google Play Services
- Device with accelerometer (for Sensor Mode)
- Location services enabled (for score tracking)
