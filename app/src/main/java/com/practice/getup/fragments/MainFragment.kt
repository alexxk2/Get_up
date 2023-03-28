package com.practice.getup.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavDirections
import androidx.navigation.findNavController
import com.google.gson.Gson
import com.practice.getup.databinding.FragmentMainBinding
import com.practice.getup.model.Options
import com.practice.getup.viewModels.MainMenuViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MainMenuViewModel by viewModels()
    private var options: Options = Options.DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
                options = it.getParcelable(OPTIONS)?: Options.DEFAULT
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

        checkForSavedOptions()
        viewModel.setOptions(options)

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

    private fun checkForSavedOptions(){
        val sharedPref = activity?.getSharedPreferences(SHARED_PREF, 0)
        val jSonDefaultOptions = Gson().toJson(Options.DEFAULT)
        val savedOptions = sharedPref?.getString(SAVED_OPTIONS,jSonDefaultOptions)
        options =  Gson().fromJson(savedOptions, Options::class.java)
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