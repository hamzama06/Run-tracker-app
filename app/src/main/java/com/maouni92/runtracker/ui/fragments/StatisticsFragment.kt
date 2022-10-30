package com.maouni92.runtracker.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.google.android.material.textview.MaterialTextView
import com.maouni92.runtracker.R
import com.maouni92.runtracker.databinding.FragmentRunBinding
import com.maouni92.runtracker.databinding.FragmentStatisticsBinding
import com.maouni92.runtracker.helper.AppExtensions.getFormattedTime
import com.maouni92.runtracker.ui.viewModels.StatisticsViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.round


@AndroidEntryPoint
class StatisticsFragment : Fragment(R.layout.fragment_statistics) {

    private var _binding: FragmentStatisticsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: StatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        viewModel.totalTimeRun.observe(viewLifecycleOwner, Observer { time ->
            time?.let {
                val totalTimeRun = time.getFormattedTime()
                binding.totalTimeTv.text = totalTimeRun
            }
        })

        viewModel.totalDistance.observe(viewLifecycleOwner, Observer { distance ->
            distance?.let {
                val km = it / 1000f
                val totalDistance = round(km * 10f) / 10f
                val totalDistanceString = "${totalDistance}km"
                binding.totalDistanceTv.text = totalDistanceString
            }
        })

        viewModel.totalAvgSpeed.observe(viewLifecycleOwner, Observer { averageSpeed ->
            averageSpeed?.let {
                val avgSpeed = round(it * 10f) / 10f
                val avgSpeedString = "${avgSpeed}km/h"
                binding.averageSpeedTv.text = avgSpeedString
            }
        })
        viewModel.totalCaloriesBurned.observe(viewLifecycleOwner, Observer { calories ->
            calories?.let {
                val totalCalories = "${it}kcal"
                binding.totalCaloriesTv.text = totalCalories
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}