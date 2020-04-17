App Name: Trending
Languages Used : Kotlin and Java
Threading Mechanism : Coroutines
Artchitecture: MVVM - Live Data, View Model, Dagger 2, RoomDb used to set up the structure.

This app fetches the repos trending on github from a Github API. Works in offline mode. This was made by me to implement MVVM in an actual application while learning the architecture.

Unit Tests written for :
1. NetworkCallHandler.kt
2. DataSourceRepository.kt
3. TrendingReposViewModel.kt

Instrument Tests written for
1. SplashActivity
2. Scenarios covering DisplayTrendingReposActivity

Instructions to run:

Method 1 :
Open in android studio and run the app module to run the actual application.

Method 2 : (Needs adb and gradlew to run)
Run the command :
`./gradlew (gradlew for windows) app:build` to build application
