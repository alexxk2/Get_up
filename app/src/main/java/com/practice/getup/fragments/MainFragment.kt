package com.practice.getup.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.practice.getup.App
import com.practice.getup.adapters.WorkoutListAdapter
import com.practice.getup.database.Workout
import com.practice.getup.databinding.FragmentMainBinding
import com.practice.getup.model.Options
import com.practice.getup.viewModels.MainMenuViewModel
import com.practice.getup.viewModels.WorkoutDatabaseViewModel
import com.practice.getup.viewModels.WorkoutDatabaseViewModelFactory


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModels()
    private var options: Options = Options.DEFAULT

    private val databaseViewModel: WorkoutDatabaseViewModel by activityViewModels {
        WorkoutDatabaseViewModelFactory(
            (activity?.application as App).workoutDatabase.workoutDao()
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            options = it.getParcelable(OPTIONS) ?: Options.DEFAULT
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = WorkoutListAdapter(requireContext(), object :
            WorkoutListAdapter.WorkoutActionListener {
            override fun onClickItem(workout: Workout) {
                Toast.makeText(context,"123", Toast.LENGTH_SHORT).show()
            }

            override fun onDeleteItem(workout: Workout) {
                Toast.makeText(context,"123", Toast.LENGTH_SHORT).show()
            }

            override fun onEditItem(workout: Workout) {
                Toast.makeText(context,"123", Toast.LENGTH_SHORT).show()
            }

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        databaseViewModel.allWorkouts.observe(viewLifecycleOwner) { allWorkouts ->
            adapter.submitList(allWorkouts)
        }

        binding.floatingButton.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToOptionsFragment()
            findNavController().navigate(action)
        }

        /*checkForSavedOptions()
        viewModel.setOptions(options)*/

        /*  binding.buttonSettings.setOnClickListener {
              val action = MainFragmentDirections.actionMainFragmentToOptionsFragment(viewModel.options.value?: Options.DEFAULT)
              navigate(action)
          }

          binding.buttonStartWorkout.setOnClickListener {
              val action = MainFragmentDirections.actionMainFragmentToWorkoutFragment(viewModel.options.value?: Options.DEFAULT)
              navigate(action)
          }

          binding.buttonWatchList.setOnClickListener {
              val action = MainFragmentDirections.actionMainFragmentToListFragment()
              navigate(action)
          }*/
    }



    private fun checkForSavedOptions() {
        val sharedPref = activity?.getSharedPreferences(SHARED_PREF, 0)
        val jSonDefaultOptions = Gson().toJson(Options.DEFAULT)
        val savedOptions = sharedPref?.getString(SAVED_OPTIONS, jSonDefaultOptions)
        options = Gson().fromJson(savedOptions, Options::class.java)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val OPTIONS = "options"
        const val SHARED_PREF = "shared_preferences"
        const val SAVED_OPTIONS = "saved_options"
    }
}