# Overview of Offset Clocks - Android

### Why?

Hello, my name is Hunter Wilhelm and welcome to my portfolio! I am trying to master the skill of making mobile applications. I wanted to focus on vanilla Android. This project has been a great way for me to learn and master Kotlin and the Android API

### Description

#### What does it do?
Offset Clocks is similar to a world clock app, but instead of changing the time zone, you can offset the clocks in the app by a few seconds, minutes, or hours. (You can even change milliseconds).

Features
* Custom clock names
* Unlimited clocks
* Show the day below the time (in the settings)
* Military / 24 hour time (in the settings)
* Edit, rename, or delete existing clocks
* Haptic feedback on edit screen to simulate clock ticking

#### What problem is this trying to solve? 
In school, I found it annoying that the school clocks were not always synced perfectly. Sometimes, the bell would ring 2 minutes earlier or later than what showed on my phone. **I wanted a way to keep track of the clocks and see them all at once.** That way, when I was getting ready for school, I could plan accordingly for how much time I needed until the bell rang. However, any app that I found would only let me change the time zone on a world clock.

Now, you can sync any clock from around the house. From your microwave clock, to your work's analog clock on the wall!

#### Demonstration / Tutorial

[Software Demo Video](http://youtube.link.goes.here)

# Development Environment

* Android Studio
* Notable Libraries in the app/build.gradle
	* [Life Cycle](https://developer.android.com/jetpack/androidx/releases/lifecycle) - LiveData and ViewModel to "store and manage UI-related data in a lifecycle conscious way" ([Overview](https://developer.android.com/topic/libraries/architecture/viewmodel))
	* [GSON](https://github.com/google/gson/blob/master/UserGuide.md) - Convert Objects <-> JSON format 

# Useful Websites

* [Android API Reference](https://developer.android.com/reference)
* [ViewModel tutorial](https://blog.mindorks.com/implementing-dialog-fragment-in-android)
* [List View tutorial 1](https://www.youtube.com/watch?v=EwwdQt3_fFU)
* [List View tutorial 2](https://www.youtube.com/watch?v=P2I8PGLZEVc)

# Future Work

* Add a camera widget on the edit screen to allow the user to sync the clock easier
* Add light mode
* Fix: Identify proper solution for opening the keyboard when the Dialog opens within the Edit Activity