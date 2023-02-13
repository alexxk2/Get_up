package com.practice.getup.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practice.getup.databinding.WorkoutItemBinding
import com.practice.getup.model.Stage

class WorkoutAdapter(private val dataSet: List<Stage>) :
    RecyclerView.Adapter<WorkoutAdapter.WorkoutViewHolder>() {


    class WorkoutViewHolder(val binding: WorkoutItemBinding) : RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WorkoutViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = WorkoutItemBinding.inflate(inflater,parent,false)

        return WorkoutViewHolder(binding)
    }


    override fun onBindViewHolder(holder: WorkoutViewHolder, position: Int) {
        val item = dataSet[position]


    }



    override fun getItemCount(): Int = dataSet.size
}