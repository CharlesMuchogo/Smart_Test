package com.charlesmuchogo.research.di

import android.content.Context
import androidx.room.Room
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepository
import com.charlesmuchogo.research.data.local.multiplatformSettings.MultiplatformSettingsRepositoryImpl
import com.charlesmuchogo.research.data.local.multiplatformSettings.PreferenceManager
import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.data.remote.RemoteRepositoryImpl
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): AppDatabase =
        Room
            .databaseBuilder(
                context,
                AppDatabase::class.java,
                "SmartApp_db",
            ).fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideSharedPreferences(
        @ApplicationContext context: Context,
    ): Settings =
        SharedPreferencesSettings( context.getSharedPreferences(
            PreferenceManager.DATASTORE_FILE_NAME,
            Context.MODE_PRIVATE,
        ))



    @Provides
    @Singleton
    fun providePreferenceManager(settings: Settings): PreferenceManager = PreferenceManager(settings = settings)

    @Provides
    @Singleton
    fun provideMultiplatformSettingsRepository(preferenceManager: PreferenceManager): MultiplatformSettingsRepository = MultiplatformSettingsRepositoryImpl(preferenceManager = preferenceManager)

    @Provides
    @Singleton
    fun provideHttp(settingsRepository: MultiplatformSettingsRepository): Http = Http(settingsRepository)


    @Provides
    @Singleton
    fun provideSnackBarViewModel(): SnackBarViewModel = SnackBarViewModel


    @Provides
    @Singleton
    fun provideApiHelper(appDatabase: AppDatabase): ApiHelper = ApiHelper(appDatabase)

    @Provides
    @Singleton
    fun provideRemoteRepository(
        apiHelper: ApiHelper,
        settingsRepository: MultiplatformSettingsRepository
    ): RemoteRepository = RemoteRepositoryImpl(apiHelper, settingsRepository)
}
