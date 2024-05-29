package com.dicoding.midsubmissionintermediate.view.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.midsubmissionintermediate.R
import com.dicoding.midsubmissionintermediate.data.pref.UserModel
import com.dicoding.midsubmissionintermediate.databinding.ActivityLoginBinding
import com.dicoding.midsubmissionintermediate.helper.ViewModelFactory
import com.dicoding.midsubmissionintermediate.view.main.MainActivity


class LoginActivity : AppCompatActivity() {
    private val viewModel by viewModels<LoginViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setupAction()
    }

    private fun setupAction() {
        with(binding){
            buttonLogin.setOnClickListener {
                val email = edLoginEmail.text.toString()
                val password = edLoginPassword.text.toString()

                viewModel.postDataLogin(email, password)
            }
        }
        viewModel.loginSuccess.observe(this){ isSuccess->
            isLoginSuccess(isSuccess)
        }
        viewModel.showLoading.observe(this){
            showLoading(it)
        }
    }
    private fun isLoginSuccess(isSuccess: Boolean){
        if (isSuccess){
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.success))
                setMessage(getString(R.string.login_success_msg))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    val email = binding.edLoginEmail.text.toString()
                    val token = viewModel.dataLogin.value?.token.toString()
                    viewModel.saveSession(UserModel(email, token))

                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.failed))
                setMessage(getString(R.string.login_failed_msg))
                setNegativeButton(getString(R.string.back)) { _, _ ->
                    return@setNegativeButton
                }
                create()
                show()
            }
        }
    }
    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
    private fun playAnimation() {
        with(binding){
            ObjectAnimator.ofFloat(ivLogin, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()


            val title = ObjectAnimator.ofFloat(tvLoginTitle, View.ALPHA, 1f).setDuration(230)
            val tvEmail = ObjectAnimator.ofFloat(tvLoginEmail, View.ALPHA, 1f).setDuration(230)
            val tvMessageEmail = ObjectAnimator.ofFloat(tvLoginMessage, View.ALPHA, 1f).setDuration(230)
            val etEmail = ObjectAnimator.ofFloat(edLoginEmail, View.ALPHA, 1f).setDuration(230)
            val tvPassword = ObjectAnimator.ofFloat(tvLoginPassword, View.ALPHA, 1f).setDuration(230)
            val etPassword = ObjectAnimator.ofFloat(edLoginPassword, View.ALPHA, 1f).setDuration(230)
            val login = ObjectAnimator.ofFloat(buttonLogin, View.ALPHA, 1f).setDuration(230)

            AnimatorSet().apply {
                playSequentially(title, tvEmail, tvMessageEmail, etEmail, tvPassword, etPassword, login)
                startDelay = 200
                start()
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        binding.pbLogin.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
}