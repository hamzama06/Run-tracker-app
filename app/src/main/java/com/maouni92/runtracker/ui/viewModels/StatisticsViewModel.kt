package com.maouni92.runtracker.ui.viewModels

import androidx.lifecycle.ViewModel
import com.maouni92.runtracker.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class StatisticsViewModel @Inject constructor(repository: Repository) : ViewModel() {


    val totalTimeRun = repository.getTotalTime()
    val totalDistance = repository.getTotalDistance()
    val totalCaloriesBurned = repository.getTotalCaloriesBurned()
    val totalAvgSpeed = repository.getTotalAvgSpeed()

    val runsSortedByDate = repository.getRunsBy("timestamp")
}