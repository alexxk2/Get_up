package com.practice.getup.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.practice.getup.databinding.WorkoutItemBinding
import com.practice.getup.model.Stage


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
    private val context: Context,
    private val dataSetInput: MutableList<Stage>
    ) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {

     var dataSet = dataSetInput
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


        holder.binding.stageName.text = item.stageName.asString(context)
        holder.binding.numberOfSetsLeft.text = item.setsLeft.asString(context)

        if (item.hasFocus) {
            //holder.binding.stageName.setTextColor(Color.parseColor("#ff5b56"))
            holder.binding.constraintLayout.animate().scaleX(1.3f)
            holder.binding.constraintLayout.animate().scaleY(1.3f)
        }
        if (!item.hasFocus) {
            //holder.binding.stageName.setTextColor(Color.parseColor("#bdbdbd"))
            holder.binding.constraintLayout.animate().scaleX(1.0f)
            holder.binding.constraintLayout.animate().scaleY(1.0f)
        }

    }

    override fun getItemCount(): Int = dataSet.size
}