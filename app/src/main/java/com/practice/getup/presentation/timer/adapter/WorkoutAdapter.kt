package com.practice.getup.presentation.timer.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.practice.getup.databinding.WorkoutItemBinding
import com.practice.getup.presentation.timer.models.Stage


class WorkoutDiffCallback(
    private val oldList: MutableList<Stage>,
    private val newList: MutableList<Stage>
): DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStage = oldList[oldItemPosition]
        val newStage = newList[newItemPosition]
        return oldStage.id == newStage.id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldStage = oldList[oldItemPosition]
        val newStage = newList[newItemPosition]
        return oldStage == newStage
    }
}

class WorkoutAdapter(
    private val context: Context
    ) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

     var dataSet = mutableListOf<Stage>()
            set(newList) {
                val diffCallback = WorkoutDiffCallback(field,newList)
                val diffResult = DiffUtil.calculateDiff(diffCallback)
                field = newList
                diffResult.dispatchUpdatesTo(this)
            }

    class WorkoutViewHolder(val binding: WorkoutItemBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WorkoutItemBinding.inflate(inflater, parent, false)
        return WorkoutViewHolder(binding)
    }


    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = dataSet[position]



        if (item.hasFocus) {
            holder.binding.stageName.setTextColor(Color.parseColor("#737373"))
            holder.binding.numberOfSetsLeft.setTextColor(Color.parseColor("#1769AA"))
            holder.binding.constraintLayout.scaleX = 1.3f
            holder.binding.constraintLayout.scaleY = 1.3f
        }
        else{
            holder.binding.stageName.setTextColor(Color.parseColor("#B8B8B8"))
            holder.binding.numberOfSetsLeft.setTextColor(Color.parseColor("#B8B8B8"))
            holder.binding.constraintLayout.scaleX = 1.0f
            holder.binding.constraintLayout.scaleY = 1.0f
        }
        holder.binding.stageName.text = item.stageName.asString(context)
        holder.binding.numberOfSetsLeft.text = item.setsLeft.asString(context)
    }

    override fun getItemCount(): Int = dataSet.size
}