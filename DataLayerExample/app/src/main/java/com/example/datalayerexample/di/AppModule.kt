package com.example.datalayerexample.di

import android.app.AlarmManager
import android.content.Context
import androidx.work.WorkManager
import com.example.datalayerexample.ExampleThread
import com.example.datalayerexample.service.PreferencesService
import com.example.datalayerexample.viewmodel.PreferencesViewModel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataLayerModule {

    @Singleton
    @Provides
    fun preferencesViewModel(@ApplicationContext app:Context) : PreferencesViewModel {
        return PreferencesViewModel(app)
    }

    @Singleton
    @Provides
    fun workManager(@ApplicationContext app:Context): WorkManager {
        return WorkManager.getInstance(app)
    }

    @Singleton
    @Provides
    fun alarmManager(@ApplicationContext app:Context): AlarmManager {
        return app.getSystemService(Context.ALARM_SERVICE) as AlarmManager
    }

    @Singleton
    @Provides
    fun preferencesService(): PreferencesService {
        return PreferencesService()
    }

    @Singleton
    @Provides
    fun exampleThread(): ExampleThread {
        return ExampleThread()
    }
}