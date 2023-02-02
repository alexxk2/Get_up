package com.practice.getup.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.practice.getup.R
import com.practice.getup.databinding.ListItemBinding
import com.practice.getup.model.Exercise


class ListAdapter(

    private val context: Context,
    private val dataSet: List<Exercise>,
    private val actionListener: ExerciseActionListener

): RecyclerView.Adapter<ListAdapter.ListViewHolder>(), View.OnClickListener{


    //create viewholder for adapter via binding
    class ListViewHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root)

    //create onClick method (necessary)
    override fun onClick(v: View?) {
        val exercise = v?.tag as Exercise
        when (v.id) {
            R.id.button_delete -> actionListener.onDeleteExercise(exercise)
            R.id.button_add -> actionListener.onAddExercise(exercise)
            else -> actionListener.onClickExercise(exercise)
        }
    }

    //standard 3 functions for recyclerview adapter are below
    //don`t forget do add clickListeners for action buttons and body of ItemView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemBinding.inflate(inflater, parent, false)

        binding.root.setOnClickListener(this)
        binding.buttonDelete.setOnClickListener(this)
        binding.buttonAdd.setOnClickListener(this)


        return ListViewHolder(binding)
    }

    //don`t forget do add tags for action buttons in item_layout
    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = dataSet[position]

        with(holder.binding) {
            itemName.text = context.resources.getString(item.stringNameId)
            itemDifficulty.text = context.resources.getString(item.stringDifficulty)

            root.tag = item
            buttonDelete.tag = item
            buttonAdd.tag = item
        }


         //TODO add normal pictures not gif
        //load gif images via Glide
        Glide.with(context).load(item.gifImageResourceId).into(holder.binding.itemGifImage)

        //change a color of background in textViewDifficulty and imageViews difficultyBricks
        when (item.stringDifficulty) {
            R.string.difficulty_easy -> {
                holder.binding.easyBrick.setBackgroundColor(Color.parseColor("#64dd17"))
                //change other bricks color to default
                holder.binding.mediumBrick.setBackgroundColor(Color.parseColor("#bdbdbd"))
                holder.binding.hardBrick.setBackgroundColor(Color.parseColor("#bdbdbd"))
            }

            R.string.difficulty_medium -> {
                holder.binding.mediumBrick.setBackgroundColor(Color.parseColor("#f4dd47"))
                //change other bricks color to default
                holder.binding.easyBrick.setBackgroundColor(Color.parseColor("#bdbdbd"))
                holder.binding.hardBrick.setBackgroundColor(Color.parseColor("#bdbdbd"))

            }

            else -> {
                holder.binding.hardBrick.setBackgroundColor(Color.parseColor("#ff5b56"))
                //change other bricks color to default
                holder.binding.easyBrick.setBackgroundColor(Color.parseColor("#bdbdbd"))
                holder.binding.mediumBrick.setBackgroundColor(Color.parseColor("#bdbdbd"))
            }
        }


    }



    override fun getItemCount() = dataSet.size

    //interface to move our functions to ListActivity. Don`t forget to put private val listener in class
    interface ExerciseActionListener{
        fun onDeleteExercise(exercise: Exercise)
        fun onClickExercise(exercise: Exercise)
        fun onAddExercise(exercise: Exercise)
    }



}

