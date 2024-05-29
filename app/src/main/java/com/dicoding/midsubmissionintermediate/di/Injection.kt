package com.dicoding.midsubmissionintermediate.di

import android.content.Context
import com.dicoding.midsubmissionintermediate.data.pref.UserPreference
import com.dicoding.midsubmissionintermediate.data.pref.dataStore
import com.dicoding.midsubmissionintermediate.data.remote.retrofit.ApiConfig
import com.dicoding.midsubmissionintermediate.data.repository.UserRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

object Injection {
    fun provideRepository(context: Context): UserRepository {

    val pref = UserPreference.getInstance(context.dataStore)

    val user = runBlocking {
        pref.getSession().first()
    }
    val apiService = ApiConfig.getApiService(user.token)

    return UserRepository.getInstance(pref, apiService)
    }

}
