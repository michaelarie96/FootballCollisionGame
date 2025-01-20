# Collision Course Android Game

A minimalist Android arcade game where players navigate between three lanes to avoid falling obstacles. The game features a minimalistic interface with responsive controls.

## Features

- Three-lane navigation system with left/right controls
- Obstacle avoidance gameplay
- Three-life system with visual indicators
- Haptic feedback and toast notifications for game events
- Custom-styled Material Design UI elements
- Automatic game restart system for continuous play

## Technical Implementation

The game is built using modern Android development practices:

- **Language**: Kotlin
- **Architecture**: Separate game logic from UI with GameManager class
- **UI Components**: MaterialButtons with custom styling
- **Animation**: Visibility state changes
- **Feedback**: Integrated vibration and toast notification systems

## Installation

To run this game on your Android device:

1. Clone this repository
2. Open the project in Android Studio
3. Build and run on your Android device or emulator

## Development

The project maintains a clean, maintainable code structure to facilitate future enhancements. Key components include:

- `MainActivity.kt`: Handles UI interactions and game loop
- `GameManager.kt`: Manages game state and logic
- `SignalManager.kt`: Controls haptic feedback and notifications

## Requirements

- Android Studio Hedgehog or later
- Minimum SDK: Android 21 (Lollipop)
- Target SDK: Android 34
- Kotlin version: 1.9.0 or later

## Acknowledgments

- Developed as part of an Android development course project
