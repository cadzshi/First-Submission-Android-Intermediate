package com.dicoding.midsubmissionintermediate.view.addstory

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.dicoding.midsubmissionintermediate.data.pref.UserModel
import com.dicoding.midsubmissionintermediate.data.remote.response.AddStoryResponse

import com.dicoding.midsubmissionintermediate.data.remote.retrofit.ApiConfig
import com.dicoding.midsubmissionintermediate.data.repository.UserRepository
import com.dicoding.midsubmissionintermediate.view.login.LoginViewModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response
import java.io.File

class AddStoryViewModel(private val repository: UserRepository): ViewModel() {

    private val _dataNewStory = MutableLiveData<AddStoryResponse?>()

    private val _uploadSuccess = MutableLiveData<Boolean>()
    val uploadSuccess : LiveData<Boolean> = _uploadSuccess

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    fun getSession(): LiveData<UserModel> {
        return repository.getSession().asLiveData()
    }

    fun postNewStory(
        imageFile: File,
        description: String,
        token: String
    ) {
        val requestBody = description.toRequestBody("text/plain".toMediaType())
        val requestImageFile = imageFile.asRequestBody("image/jpeg".toMediaType())
        val multipartBody = MultipartBody.Part.createFormData(
            "photo",
            imageFile.name,
            requestImageFile
        )
        _showLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getApiService(token).addNewStory(multipartBody, requestBody)
            client.enqueue(object : Callback<AddStoryResponse> {
                override fun onResponse(
                    call: Call<AddStoryResponse>,
                    response: Response<AddStoryResponse>
                ) {
                    _showLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _dataNewStory.value = responseBody
                            _uploadSuccess.value = true
                        }
                    } else {
                        _uploadSuccess.value = false
                        Log.e(LoginViewModel.TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<AddStoryResponse>, t: Throwable) {
                    _showLoading.value = false
                    Log.e(LoginViewModel.TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
}

