package com.maouni92.runtracker.ui.viewModels

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.maouni92.runtracker.BuildConfig
import com.maouni92.runtracker.data.Run
import com.maouni92.runtracker.helper.SortType
import com.maouni92.runtracker.repositories.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor( val repository: Repository)  : ViewModel() {

    private val runsSortedByDate = repository.getRunsBy("timestamp")
    private val runsSortedByDistance = repository.getRunsBy("distance")
    private val runsSortedByCaloriesBurned = repository.getRunsBy("caloriesBurned")
    private val runsSortedByTimeInMillis = repository.getRunsBy("time")
    private val runsSortedByAvgSpeed = repository.getRunsBy("averageSpeed")

    val runs = MediatorLiveData<List<Run>>()
    var sortType = SortType.DATE

    init {
        runs.addSource(runsSortedByDate) { result ->
            if(sortType == SortType.DATE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByAvgSpeed) { result ->
            if(sortType == SortType.AVG_SPEED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByCaloriesBurned) { result ->
            if(sortType == SortType.CALORIES_BURNED) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByDistance) { result ->
            if(sortType == SortType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
        runs.addSource(runsSortedByTimeInMillis) { result ->
            if(sortType == SortType.RUNNING_TIME) {
                result?.let { runs.value = it }

            }
        }
    }

    fun sortRuns(sortType: SortType) = when(sortType) {
        SortType.DATE -> runsSortedByDate.value?.let { runs.value = it }
        SortType.RUNNING_TIME -> runsSortedByTimeInMillis.value?.let { runs.value = it }
        SortType.AVG_SPEED -> runsSortedByAvgSpeed.value?.let { runs.value = it }
        SortType.DISTANCE -> runsSortedByDistance.value?.let { runs.value = it }
        SortType.CALORIES_BURNED -> runsSortedByCaloriesBurned.value?.let { runs.value = it }
    }.also {
        this.sortType = sortType
    }

  //  val runsSortedByDate = repository.getRunsBy("timestamp")

    fun insertRun(run: Run) = viewModelScope.launch {
        repository.insertRun(run)
    }

    fun deleteRun(run : Run) = viewModelScope.launch {
        repository.deleteRun(run)
    }
}