package com.maouni92.runtracker.di

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.room.Room
import com.maouni92.runtracker.data.RunDatabase
import com.maouni92.runtracker.helper.Constants
import com.maouni92.runtracker.helper.Constants.KEY_FIRST_TIME_TOGGLE
import com.maouni92.runtracker.helper.Constants.KEY_NAME
import com.maouni92.runtracker.helper.Constants.KEY_WEIGHT
import com.maouni92.runtracker.helper.Constants.SHARED_PREFERENCES_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AppModule {


    @Singleton
    @Provides
    fun provideRunDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context,
        RunDatabase::class.java,
        Constants.RUN_DATABASE_NAME).build()

    @Singleton
    @Provides
    fun provideRunDAO(db:RunDatabase) = db.getRunDAO()

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext app: Context) =
        app.getSharedPreferences(SHARED_PREFERENCES_NAME, MODE_PRIVATE)

    @Singleton
    @Provides
    fun provideName(sharedPref: SharedPreferences) = sharedPref.getString(KEY_NAME, "") ?: ""

    @Singleton
    @Provides
    fun provideWeight(sharedPref: SharedPreferences) = sharedPref.getFloat(KEY_WEIGHT, 75f)

    @Singleton
    @Provides
    fun provideFirstTimeToggle(sharedPref: SharedPreferences) =
        sharedPref.getBoolean(KEY_FIRST_TIME_TOGGLE, true)
}