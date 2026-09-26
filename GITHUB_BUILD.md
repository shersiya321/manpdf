# PDF Editor - GitHub APK Build

1. Upload all files in this folder to the ROOT of a GitHub repository.
2. Open the repository's **Actions** tab.
3. Select **Build APK**.
4. Press **Run workflow**.
5. When the run is green, open it and download **PDFEditor-debug-apk** under Artifacts.
6. Extract the downloaded artifact and install `app-debug.apk` on Android.

Do not upload this project as one ZIP file inside GitHub. Upload the extracted files/folders so `.github/workflows/build-apk.yml`, `app/`, `gradle/`, `gradlew`, `settings.gradle.kts`, etc. are visible at the repository root.
