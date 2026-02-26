package com.example.freemanstats.di

import android.content.Context
import com.example.freemanstats.data.ClanPreferences
import com.example.freemanstats.data.ClanPreferencesImpl
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
    fun provideClanPreferences(@ApplicationContext context: Context): ClanPreferences {
        return ClanPreferencesImpl(context)
    }
}