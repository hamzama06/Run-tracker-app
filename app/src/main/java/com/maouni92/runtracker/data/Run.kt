package com.maouni92.runtracker.data

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "run_table")
data class Run(
    var image: Bitmap? = null,
    var timestamp: Long = 0L,
    var averageSpeed: Float = 0f, // in Kmh
    var distance: Int = 0,     // in meters
    var time: Long = 0L,   // in milliseconds
    var caloriesBurned: Int = 0
){
    @PrimaryKey(autoGenerate = true)
    var id : Int? = null
}
