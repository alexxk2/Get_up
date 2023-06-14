package com.practice.getup.presentation.main_menu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
        RecyclerView.ViewHolder(binding.root)


    override fun onClick(v: View?) {
        val workout = v?.tag as Workout
        when(v.id){
            R.id.item_edit_button -> actionListener.onEditItem(workout)
            R.id.item_start_workout -> actionListener.onStartItem(workout)
            else -> actionListener.onClickItem(workout)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): WorkoutListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WorkoutListItemBinding.inflate(inflater,parent,false)

        binding.root.setOnClickListener(this)
        binding.itemStartWorkout.setOnClickListener(this)
        binding.itemEditButton.setOnClickListener(this)

        return WorkoutListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WorkoutListViewHolder, position: Int) {
        val item = getItem(position)
        with(holder.binding) {
            workoutName.text = item.name
            totalTime.text = context.getString(R.string.total_workout_time, getTotalTime(item) )
            root.tag = item
            itemEditButton.tag = item
            itemStartWorkout.tag = item
        }
    }

    interface WorkoutActionListener {
        fun onClickItem(workout: Workout)
        fun onEditItem(workout: Workout)
        fun onStartItem(workout: Workout)
    }

    private fun getTotalTime(workout: Workout): String {
        val totalSeconds = with(workout) {
            preparingTime + (workTime + restTime) * numberOfSets
        }
        val secondsToShow = (totalSeconds % 60).toString().padStart(2, '0')
        val minutesToShow = ((totalSeconds / 60) % 60).toString().padStart(2, '0')
        val hoursToShow = (totalSeconds / 3600).toString().padStart(2, '0')

        return when{
            (totalSeconds/3600)>0 -> "$hoursToShow:$minutesToShow:$secondsToShow"
            else -> "$minutesToShow : $secondsToShow"
        }
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