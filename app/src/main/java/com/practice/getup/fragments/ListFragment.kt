package com.practice.getup.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.view.get
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.practice.getup.R
import com.practice.getup.adapters.ListAdapter
import com.practice.getup.data.DataSource
import com.practice.getup.databinding.FragmentListBinding
import com.practice.getup.model.Exercise


class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentListBinding.inflate(layoutInflater, container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = ListAdapter(requireContext(),data, object: ListAdapter.ExerciseActionListener{
            override fun onDeleteExercise(exercise: Exercise) {
                Snackbar.make(binding.recyclerView, R.string.snackbar_delete, Snackbar.LENGTH_SHORT)
                    .setAction(R.string.snackbar_got_it) {}
                    .show()

                //TODO работает, но надо фиксить чтобы анимация не пропадала
                binding.recyclerView.smoothScrollToPosition(4)
                binding.recyclerView[4].animation = AnimationUtils.loadAnimation(context, R.anim.has_focus_animation)
                binding.recyclerView[4].scaleX =1.2f
                binding.recyclerView[4].scaleY =1.2f
            }

            override fun onClickExercise(exercise: Exercise) {
                val action = ListFragmentDirections.actionListFragmentToDetailFragment(exercise.videoResource)
                binding.root.findNavController().navigate(action)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}