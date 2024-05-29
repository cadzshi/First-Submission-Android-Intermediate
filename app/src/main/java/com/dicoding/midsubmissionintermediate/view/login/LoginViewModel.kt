package com.dicoding.midsubmissionintermediate.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.midsubmissionintermediate.data.pref.UserModel
import com.dicoding.midsubmissionintermediate.data.remote.response.LoginResponse
import com.dicoding.midsubmissionintermediate.data.remote.response.LoginResult
import com.dicoding.midsubmissionintermediate.data.remote.retrofit.ApiConfig
import com.dicoding.midsubmissionintermediate.data.repository.UserRepository
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginViewModel(private val repository: UserRepository) : ViewModel() {

    private val _dataLogin = MutableLiveData<LoginResult?>()
    val dataLogin : LiveData<LoginResult?> = _dataLogin

    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess : LiveData<Boolean> = _loginSuccess

    private val _showLoading = MutableLiveData<Boolean>()
    val showLoading : LiveData<Boolean> = _showLoading

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }

    fun postDataLogin(email: String, password: String) {
        _showLoading.value = true
        viewModelScope.launch {
            val client = ApiConfig.getApiService().login(email, password)
            client.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                _showLoading.value = false
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        if (responseBody != null) {
                            _dataLogin.value = responseBody.loginResult
                            _loginSuccess.value = true
                            Log.d(TAG, "onResponse: ${responseBody.loginResult?.token}")
                        }
                    } else {
                        _loginSuccess.value = false
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _showLoading.value = false
                    Log.e(TAG, "onFailure: ${t.message}")
                }
            })
        }
    }
    companion object{
        const val TAG = "LoginViewModel"
    }
}