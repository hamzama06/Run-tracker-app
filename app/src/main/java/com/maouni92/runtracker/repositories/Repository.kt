package com.maouni92.runtracker.repositories

import com.maouni92.runtracker.data.Run
import com.maouni92.runtracker.data.RunDAO
import javax.inject.Inject

class Repository @Inject constructor(private val dao: RunDAO) {

    suspend fun insertRun(run : Run) = dao.insertRun(run)

    suspend fun deleteRun(run: Run) = dao.deleteRun(run)

    fun getRunsBy(column:String) = dao.getRunsBy(column)

    fun getTotalTime() = dao.getTotalTime()

    fun getTotalCaloriesBurned() = dao.getTotalCaloriesBurned()

    fun getTotalDistance() = dao.getTotalDistance()

    fun getTotalAvgSpeed() = dao.getTotalAvgSpeed()
}