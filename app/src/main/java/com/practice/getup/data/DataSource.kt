package com.practice.getup.data

import com.practice.getup.R
import com.practice.getup.model.Exercise

class DataSource {

    fun loadExercise(): List<Exercise> {

        return listOf(
            Exercise(
                R.string.exercise1,
                R.string.exercise1_description,
                R.string.difficulty_easy,
                R.raw.prone_position,
                R.raw.prone_position_video
            ),
            Exercise(
                R.string.exercise2,
                R.string.exercise2_description,
                R.string.difficulty_easy,
                R.raw.pushups,
                R.raw.pushups_video
            ),
            Exercise(
                R.string.exercise3,
                R.string.exercise3_description,
                R.string.difficulty_easy,
                R.raw.squats,
                R.raw.squats_video
            ),
            Exercise(
                R.string.exercise4,
                R.string.exercise4_description,
                R.string.difficulty_easy,
                R.raw.crunches,
                R.raw.crunches_video
            ),
            Exercise(
                R.string.exercise5,
                R.string.exercise5_description,
                R.string.difficulty_medium,
                R.raw.lunges,
                R.raw.lunges_video
            ),
            Exercise(
                R.string.exercise6,
                R.string.exercise6_description,
                R.string.difficulty_medium,
                R.raw.crunches_hard,
                R.raw.crunches_hard_video
            ),
            Exercise(
                R.string.exercise7,
                R.string.exercise7_description,
                R.string.difficulty_medium,
                R.raw.plank,
                R.raw.plank_video
            ),
            Exercise(
                R.string.exercise8,
                R.string.exercise8_description,
                R.string.difficulty_medium,
                R.raw.slow_pushups,
                R.raw.slow_pushups_video
            ),
            Exercise(
                R.string.exercise9,
                R.string.exercise9_description,
                R.string.difficulty_hard,
                R.raw.hard_squats,
                R.raw.hard_squats_video
            ),
            Exercise(
                R.string.exercise10,
                R.string.exercise10_description,
                R.string.difficulty_hard,
                R.raw.hard_plank,
                R.raw.hard_plank_video
            ),
            Exercise(
                R.string.exercise11,
                R.string.exercise11_description,
                R.string.difficulty_hard,
                R.raw.diamond_pushup,
                R.raw.diamond_pushups_video
            ),
            Exercise(
                R.string.exercise12,
                R.string.exercise12_description,
                R.string.difficulty_hard,
                R.raw.climber,
                R.raw.climber_video
            )
        )
    }

    fun loadEasyList(): List<Exercise> {
        return loadExercise().filter { it.stringDifficulty == R.string.difficulty_easy }
    }
    fun loadMediumList(): List<Exercise> {
        return loadExercise().filter { it.stringDifficulty == R.string.difficulty_medium }
    }
    fun loadHardList(): List<Exercise> {
        return loadExercise().filter { it.stringDifficulty == R.string.difficulty_hard }
    }
}