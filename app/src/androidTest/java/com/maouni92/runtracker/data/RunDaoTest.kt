package com.maouni92.runtracker.data

import android.graphics.Bitmap
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import com.maouni92.runtracker.util.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@SmallTest
@ExperimentalCoroutinesApi
class RunDaoTest {

    @get:Rule
    val instanceTaskExecutor = InstantTaskExecutorRule()

    lateinit var db:RunDatabase
    lateinit var dao:RunDAO
    lateinit var bmp:Bitmap

    @Before
    fun setup(){
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), RunDatabase::class.java)
            .allowMainThreadQueries()
            .build()
      dao = db.getRunDAO()

        val conf = Bitmap.Config.ARGB_8888
         bmp = Bitmap.createBitmap(120, 200, conf)
    }

    @Test
    fun insertRun_should_validDistance() = runTest{
        val run = Run(bmp, 500L, 150f, 1500, 1500L, 120)
        dao.insertRun(run)
        val runs = dao.getRunsBy("time").getOrAwaitValue()

        assertThat(runs[0].distance).isEqualTo(run.distance)

    }

    @Test
    fun deleteRun_shouldReturn_validSize()= runTest{
        val run = Run(bmp, 500L, 150f, 1500, 1500L, 120)
        dao.insertRun(run)
        val beforeDeleteRuns = dao.getRunsBy("time").getOrAwaitValue()
        dao.deleteRun(beforeDeleteRuns[0])
        val afterDeleteRuns = dao.getRunsBy("time").getOrAwaitValue()


        assertThat(afterDeleteRuns.size).isEqualTo(0)
    }

    @Test
    fun getTotalDistance_shouldReturn_validTotal()= runTest{
        val run = Run(bmp, 500L, 150f, 1500, 1500L, 120)
        val run2 = Run(bmp, 500L, 150f, 2000, 1500L, 120)
        dao.insertRun(run)
        dao.insertRun(run2)
       val expectedTotalDistance = 3500
        val currentTotalDistance = dao.getTotalDistance().getOrAwaitValue()
        assertThat(currentTotalDistance).isEqualTo(expectedTotalDistance)

    }

    @Test
    fun getTotalTime_shouldReturn_validTotal()= runTest{
        val run = Run(bmp, 500L, 150f, 1500, 1500L, 120)
        val run2 = Run(bmp, 500L, 150f, 1500, 1000L, 120)
        dao.insertRun(run)
        dao.insertRun(run2)
        val expectedTotalTime = 2500
        val currentTotalTime = dao.getTotalTime().getOrAwaitValue()
        assertThat(currentTotalTime).isEqualTo(expectedTotalTime)
    }

    @Test
    fun getTotalCalories_shouldReturn_validTotal()= runTest{
        val run = Run(bmp, 500L, 150f, 1500, 1500L, 120)
        val run2 = Run(bmp, 500L, 150f, 1500, 1500L, 300)
        dao.insertRun(run)
        dao.insertRun(run2)
        val expectedTotalCalories = 420
        val currentTotalCalories = dao.getTotalCaloriesBurned().getOrAwaitValue()
        assertThat(currentTotalCalories).isEqualTo(expectedTotalCalories)
    }


    @After
    fun closeDatabase(){
        db.close()
    }
}