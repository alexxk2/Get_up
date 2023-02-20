package com.practice.getup.adapters

import android.content.Context
import android.content.res.Resources
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.practice.getup.R
import com.practice.getup.databinding.WorkoutItemBinding
import com.practice.getup.model.Stage
import com.practice.getup.activities.UiText

class WorkoutAdapter(
    private val context: Context,
    private val dataSet: List<Stage>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {


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