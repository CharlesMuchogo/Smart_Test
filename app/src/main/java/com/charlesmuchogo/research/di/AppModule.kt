package com.charlesmuchogo.research.di

import android.content.Context
import androidx.room.Room
import com.charlesmuchogo.research.data.local.AppDatabase
import com.charlesmuchogo.research.data.network.ApiHelper
import com.charlesmuchogo.research.data.network.Http
import com.charlesmuchogo.research.data.remote.RemoteRepository
import com.charlesmuchogo.research.data.remote.RemoteRepositoryImpl
import com.charlesmuchogo.research.domain.viewmodels.SnackBarViewModel
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
    fun provideHttp(appDatabase: AppDatabase): Http = Http(appDatabase)


    @Provides
    @Singleton
    fun provideSnackBarViewModel(): SnackBarViewModel = SnackBarViewModel()


    @Provides
    @Singleton
    fun provideApiHelper(): ApiHelper = ApiHelper()

    @Provides
    @Singleton
    fun provideRemoteRepository(
        apiHelper: ApiHelper,
        appDatabase: AppDatabase,
    ): RemoteRepository = RemoteRepositoryImpl(apiHelper, appDatabase)
}
