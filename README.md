# FitnessApp

## Overview

FitnessApp is an Android application designed to guide users through structured workout routines that integrate Electrical Muscle Stimulation (EMS) via Bluetooth. The app manages exercise sets and repetitions with visual feedback, automatically controls timing, and sends EMS commands to a connected device throughout the workout session.

This app is intended for scenarios such as rehabilitation training, strength conditioning, or EMS-assisted workouts.

## Features

- Bluetooth-based EMS communication
- Automatic timer-based rep and set management
- Circular progress indicator for rep completion
- Wake lock support to prevent screen sleep during workouts
- Singleton pattern for shared configuration and BLE instance
- Activity transitions for rest periods and workout completion

## Architecture

```

MainActivity (not shown)
↓
ExerciseActivity
↳ Controls workout execution and EMS signals
↳ Transitions to RestActivity between sets
↳ Finishes with ExerciseDoneActivity after final set

````

## Components

### ExerciseActivity.java

Handles the active exercise session:
- Tracks number of reps and sets
- Updates UI using a circular seek bar
- Sends EMS signals through a Bluetooth interface
- Transitions to rest or completion screens
- Acquires and releases a WakeLock to keep the screen on

### SingletonData.java

Stores global configuration data:
- Rep count
- Time interval between reps
- Accessed across activities via the singleton pattern

### Singleton.java

Provides a globally accessible instance of the Bluetooth EMS object:
- Prevents multiple reconnections to the EMS device
- Ensures consistent BLE state across activities

### Bluetooth.java

Handles low-level Bluetooth communication with the EMS device:
- `startAdd(String command)` sends EMS signals based on current rep or set status

## Dependencies Used

- AndroidX AppCompat
- [CircularSeekBar](https://github.com/Tankery/CircularSeekBar) by Tankery
- Android SDK API 21 or higher
