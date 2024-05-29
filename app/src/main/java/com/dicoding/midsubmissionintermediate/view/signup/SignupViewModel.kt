package com.dicoding.midsubmissionintermediate.view.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.midsubmissionintermediate.data.remote.response.RegisterResponse
import com.dicoding.midsubmissionintermediate.data.remote.retrofit.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupViewModel: ViewModel() {

    private val _registerSuccess = MutableLiveData<Boolean>()
    val registerSuccess : LiveData<Boolean> = _registerSuccess

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading


    fun postDataRegister(name: String, email: String, password: String) {
        _showLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getApiService().register(name, email, password)
            client.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    _showLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _registerSuccess.value = true
                        }
                    } else {
                        _registerSuccess.value = false
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    _showLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }

    }

    companion object{
        const val TAG = "SignupViewModel"
    }
}
