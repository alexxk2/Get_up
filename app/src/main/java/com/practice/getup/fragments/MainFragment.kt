package com.practice.getup.fragments


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.practice.getup.activities.MainActivity
import com.practice.getup.databinding.FragmentMainBinding
import com.practice.getup.model.Options
import com.practice.getup.viewModels.MainMenuViewModel


class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainMenuViewModel by viewModels()

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


        //TODO сделать onClickListeners  для всех кнопок меню, когда сделаю navFragment and navGraph
        //также принять объект options из опций и присвоить его с помощью метода из viewModel

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val KEY_OPTIONS = "OPTIONS"
        const val BACK_OPTIONS = "BACK OPTIONS"
    }
}