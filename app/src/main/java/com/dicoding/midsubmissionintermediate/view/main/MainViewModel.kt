package com.dicoding.midsubmissionintermediate.view.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.midsubmissionintermediate.data.pref.UserModel
import com.dicoding.midsubmissionintermediate.data.remote.response.ListStoryItem
import com.dicoding.midsubmissionintermediate.data.remote.response.StoryResponse
import com.dicoding.midsubmissionintermediate.data.remote.retrofit.ApiConfig
import com.dicoding.midsubmissionintermediate.data.repository.UserRepository
import com.dicoding.midsubmissionintermediate.view.login.LoginViewModel
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val repository: UserRepository) : ViewModel() {

    private val _dataStories = MutableLiveData<List<ListStoryItem>>()
    val dataStories : LiveData<List<ListStoryItem>> = _dataStories

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }

    fun getListStories(token: String){
        _showLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getApiService(token).getStories()
            client.enqueue(object : Callback<StoryResponse> {
                override fun onResponse(
                    call: Call<StoryResponse>,
                    response: Response<StoryResponse>
                ) {
                    _showLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _dataStories.value = responseBody.listStory
                        }
                    } else {
                        Log.e(LoginViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<StoryResponse>, t: Throwable) {
                    _showLoading.value = false
                    Log.e(LoginViewModel.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
}