package com.practice.getup.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.practice.getup.R
import com.practice.getup.adapters.ListAdapter
import com.practice.getup.data.DataSource
import com.practice.getup.databinding.ActivityListBinding
import com.practice.getup.model.Exercise

class ListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadRecycleView(DataSource().loadExercise())

        //TODO когда отжимаешь все чипы то надо, чтобы все пропадало или сделать, чтобы all levels
        //было не отжимаемым
        with(binding)
        {
            chipEasy.setOnClickListener {
                loadRecycleView(DataSource().loadEasyList())
            }
            chipMedium.setOnClickListener {
                loadRecycleView(DataSource().loadMediumList())
            }
            chipHard.setOnClickListener {
                loadRecycleView(DataSource().loadHardList())
            }
            chipAll.setOnClickListener {
                loadRecycleView(DataSource().loadExercise())
            }
        }
    }

    private fun loadRecycleView(data: List<Exercise>) {

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = ListAdapter(this,data, object: ListAdapter.ExerciseActionListener{
            override fun onDeleteExercise(exercise: Exercise) {
                Snackbar.make(binding.recyclerView, R.string.snackbar_delete, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_got_it) {}
                    .show()

                //работает, но надо фиксить чтобы анимация не пропадала
                binding.recyclerView.smoothScrollToPosition(4)
                binding.recyclerView[4].animation = AnimationUtils.loadAnimation(this@ListActivity, R.anim.has_focus_animation)

            }
            override fun onClickExercise(exercise: Exercise) {
                val intent = Intent(this@ListActivity, DetailActivity::class.java)
                intent.putExtra(DetailActivity.VIDEO_ID,exercise.videoResource)
                startActivity(intent)
            }
            override fun onAddExercise(exercise: Exercise) {
                Snackbar.make(binding.recyclerView, R.string.snackbar_delete, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_got_it) {}
                    .show()
            }
        })

        binding.recyclerView.startLayoutAnimation()
        binding.recyclerView.setHasFixedSize(true)
    }
}