package com.practice.getup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.practice.getup.R
import com.practice.getup.database.Workout
import com.practice.getup.databinding.WorkoutListItemBinding


class WorkoutListAdapter(
    private val context: Context,
    private val actionListener: WorkoutActionListener
) : ListAdapter<Workout, WorkoutListAdapter.WorkoutListViewHolder>(DiffCallBack),
    View.OnClickListener {


    class WorkoutListViewHolder(val binding: WorkoutListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }

    override fun onClick(v: View?) {
        val workout = v?.tag as Workout
        when(v.id){
            R.id.edit_button -> actionListener.onEditItem(workout)
            R.id.delete_button -> actionListener.onDeleteItem(workout)
            else -> actionListener.onClickItem(workout)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkoutListViewHolder {
        return WorkoutListViewHolder(
            binding = WorkoutListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent,false
            )
        )
    }

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            workoutName.text = item.name
            totalTime.text = context.getString(R.string.total_workout_time, getTotalTime(item) )
            root.tag = item
            deleteButton.tag = item
            editButton.tag = item
        }
    }

    interface WorkoutActionListener {
        fun onClickItem(workout: Workout)
        fun onDeleteItem(workout: Workout)
        fun onEditItem(workout: Workout)
    }

    private fun getTotalTime(workout: Workout): String {
        val totalSeconds = with(workout) {
            preparingTime + (workTime + restTime) * numberOfSets
        }
        val secondsToShow = totalSeconds % 60
        val minutesToShow = (totalSeconds / 60) % 60
        val hoursToShow = totalSeconds / 3600

        return "$hoursToShow:$minutesToShow:$secondsToShow"
    }

    companion object{
        val DiffCallBack = object : DiffUtil.ItemCallback<Workout>() {

            override fun areItemsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: Workout, newItem: Workout): Boolean {
                return oldItem == newItem
            }

        }
    }

}