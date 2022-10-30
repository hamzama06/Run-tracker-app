package com.maouni92.runtracker.data

import androidx.lifecycle.LiveData
import androidx.room.*


@Dao
interface RunDAO {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run:Run)

    @Delete
    suspend fun deleteRun(run:Run)

    @Query("SELECT * FROM run_table\n" +
            "ORDER BY \n" +
            "CASE WHEN :column = 'timestamp'  THEN timestamp END DESC,\n" +
            "CASE WHEN :column = 'time' THEN time END DESC,\n" +
            "CASE WHEN :column = 'caloriesBurned' THEN caloriesBurned END DESC,\n" +
            "CASE WHEN :column = 'averageSpeed'  THEN averageSpeed END DESC,\n" +
            "CASE WHEN :column = 'distance' THEN distance END DESC")
    fun getRunsBy(column:String): LiveData<List<Run>>

    @Query("SELECT SUM(time) FROM run_table")
    fun getTotalTime(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM run_table")
    fun getTotalCaloriesBurned(): LiveData<Int>

    @Query("SELECT SUM(distance) FROM run_table")
    fun getTotalDistance(): LiveData<Int>

    @Query("SELECT AVG(averageSpeed) FROM run_table")
    fun getTotalAvgSpeed(): LiveData<Float>


}