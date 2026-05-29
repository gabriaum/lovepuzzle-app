# Love Puzzle App

A romantic Android puzzle game built with Kotlin.

Users answer a sequence of questions, unlock levels, and reach a final celebration screen with animations.
Progress is persisted locally, and the app includes a daily progression limit.

## Features

- Splash screen with animated gradient and pulsing heart.
- Puzzle flow with question-by-question progression.
- Local progress persistence with SQLite (`level` and cooldown state).
- Daily limit system (up to 5 progressed levels before a 24-hour lock).
- Optional Discord webhook hook points for success/wrong answer events.
- Final celebration screen with confetti, typewriter text, and floating animations.

## Tech Stack

- Kotlin
- Android SDK (`compileSdk = 36`, `minSdk = 34`, `targetSdk = 36`)
- AndroidX + Material Components
- OkHttp (for webhook requests)
- SQLite via `openOrCreateDatabase`
- 
## Getting Started

### Prerequisites

- Android Studio (latest stable recommended)
- JDK 11
- Android SDK 36 installed
- Android device/emulator running Android 14+ (API 34 or higher)

### Run in Android Studio

1. Open the project folder.
2. Let Gradle sync finish.
3. Select the `app` run configuration.
4. Run on an emulator or physical device.

### Run from command line

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat test
```

## Configuration

### Puzzle content

Edit `ResponseManager` to customize questions and expected answers:

- File: `app/src/main/java/com/gabriaum/order/manager/ResponseManager.kt`

### Daily limit

Current behavior in `MainActivity` + `DailyLimitService`:

- Max progressed levels before cooldown: `5`
- Cooldown duration: `24 hours`

### Discord webhook (optional)

`WebhookService` is already integrated, but webhook URL arguments are currently empty strings in `MainActivity`.

To enable notifications, pass your webhook URL where `sendDiscordWebhook(...)` is called:

- File: `app/src/main/java/com/gabriaum/order/ui/MainActivity.kt`