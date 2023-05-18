package com.practice.getup.fragments


import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.practice.getup.App
import com.practice.getup.R
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
                Snackbar.make(view,"123",20000)
                    .setAction("OK"){}
                    .show()
            }

            override fun onStartItem(workout: Workout) {
                val action = MainFragmentDirections.actionMainFragmentToWorkoutFragment(workout = workout)
                findNavController().navigate(action)
            }

            override fun onEditItem(workout: Workout) {
                val action = MainFragmentDirections.actionMainFragmentToOptionsFragment(id = workout.id)
                findNavController().navigate(action)
            }

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        databaseViewModel.allWorkouts.observe(viewLifecycleOwner) { allWorkouts ->
            adapter.submitList(allWorkouts)
            manageEmptyListUi(allWorkouts)
        }

        binding.floatingButtonAdd.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToOptionsFragment()
            findNavController().navigate(action)
        }

        binding.floatingButtonDelete.setOnClickListener { showDeleteAllConfirmationDialog()}

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

    private fun manageEmptyListUi(list: List<Workout>){
        if (list.isEmpty()){
            binding.recyclerView.visibility = View.GONE
            binding.floatingButtonDelete.visibility = View.GONE
            binding.emptyListViews.visibility = View.VISIBLE

        }

        else {
            binding.emptyListViews.visibility = View.GONE
            binding.recyclerView.visibility = View.VISIBLE
            binding.floatingButtonDelete.visibility = View.VISIBLE

        }
    }

    private fun showDeleteAllConfirmationDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.alert_dialog_title))
            .setMessage(getString(R.string.alert_dialog_message_delete_all))
            .setCancelable(false)
            .setNegativeButton(getString(R.string.answer_no)) { _, _ -> }
            .setPositiveButton(getString(R.string.answer_yes)) { _, _ ->
                databaseViewModel.deleteAll()
            }
            .show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val OPTIONS = "options"
        const val SHARED_PREF = "shared_preferences"
        const val SAVED_OPTIONS = "saved_options"
        const val ID = "id"
    }
}