package com.practice.getup.presentation.main_menu.ui


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.practice.getup.R
import com.practice.getup.domain.models.Workout
import com.practice.getup.databinding.FragmentMainBinding
import com.practice.getup.presentation.main_menu.view_model.MainMenuViewModel
import com.practice.getup.presentation.main_menu.adapter.WorkoutListAdapter
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(viewModel){
            setAppDayOrNightMode()
            setAppLanguage()
        }

        val adapter = WorkoutListAdapter(requireContext(), object :
            WorkoutListAdapter.WorkoutActionListener {
            override fun onClickItem(workout: Workout) {

            }

            override fun onStartItem(workout: Workout) {
                val action =
                    MainFragmentDirections.actionMainFragmentToTimerFragment(workout = workout)
                navigate(action)
            }

            override fun onEditItem(workout: Workout) {
                val action =
                    MainFragmentDirections.actionMainFragmentToOptionsFragment(id = workout.id)
                navigate(action)
            }

        })

        binding.recyclerView.adapter = adapter
        binding.recyclerView.setHasFixedSize(true)
        viewModel.allWorkouts.observe(viewLifecycleOwner) { allWorkouts ->
            adapter.submitList(allWorkouts)
            manageEmptyListUi(allWorkouts)
        }

        binding.floatingButtonAdd.setOnClickListener {
            val action = MainFragmentDirections.actionMainFragmentToOptionsFragment()
            findNavController().navigate(action)
        }

        binding.floatingButtonDelete.setOnClickListener { showDeleteAllConfirmationDialog()}

        binding.topAppBar.setOnMenuItemClickListener {menuItem ->
            when(menuItem.itemId){
                R.id.settings -> {
                    val action = MainFragmentDirections.actionMainFragmentToSettingsFragment()
                    navigate(action)
                    true
                }
                else ->false
            }
        }
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
                viewModel.deleteAll()
            }
            .show()
    }

    private fun navigate(action: NavDirections){
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}