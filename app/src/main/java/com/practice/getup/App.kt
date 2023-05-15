package com.practice.getup

import android.app.Application
import com.practice.getup.database.WorkoutDatabase

class App: Application() {
    val workoutDatabase: WorkoutDatabase by lazy { WorkoutDatabase.getDataBase(this) }
}