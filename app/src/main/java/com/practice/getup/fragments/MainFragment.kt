package com.practice.getup.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import androidx.navigation.fragment.FragmentNavigatorDestinationBuilder
import com.practice.getup.activities.MainActivity
import com.practice.getup.databinding.FragmentMainBinding
import com.practice.getup.model.Options
import com.practice.getup.viewModels.MainMenuViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModels()
    private var options: Options? = Options.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
                options = it.getParcelable(OPTIONS)
        }
        viewModel.setOptions(options?: Options.DEFAULT)
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


        binding.buttonSettings.setOnClickListener {
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
        }
    }

    private fun navigate(action: NavDirections){
        binding.root.findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val OPTIONS = "options"

    }
}