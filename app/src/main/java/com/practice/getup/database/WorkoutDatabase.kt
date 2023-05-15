package com.practice.getup.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Workout::class], version = 1, exportSchema = false)
abstract class WorkoutDatabase() : RoomDatabase() {

    abstract fun workoutDao(): WorkoutDao

    companion object{
        private var INSTANCE: WorkoutDatabase? = null

        fun getDataBase(context: Context): WorkoutDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context,
                    WorkoutDatabase::class.java,
                    "workout_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                return instance
            }
        }
    }
}