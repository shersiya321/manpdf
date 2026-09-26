# PDF Editor - Android Studio Ready

This is a complete Android Studio project for the PDF Editor app.

## Open
1. Extract this ZIP.
2. Open the folder containing `settings.gradle.kts` in Android Studio.
3. Let Gradle Sync finish. The included Gradle wrapper uses Gradle 8.9.
4. If Android Studio asks to install missing SDK components, accept the installation.
5. Build > Build Bundle(s) / APK(s) > Build APK(s).

APK: `app/build/outputs/apk/debug/app-debug.apk`

## Features
- Open PDF from device
- Page navigation
- Pen drawing
- Highlighter
- Eraser
- Undo / Clear
- Editor toolbar on left/right/top/bottom
- No screen recording
- No microphone permission

## Note
Annotations are currently an on-screen overlay; they are not exported back into the PDF file.
