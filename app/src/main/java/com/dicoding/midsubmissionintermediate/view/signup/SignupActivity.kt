package com.dicoding.midsubmissionintermediate.view.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dicoding.midsubmissionintermediate.R
import com.dicoding.midsubmissionintermediate.data.repository.UserRepository
import com.dicoding.midsubmissionintermediate.databinding.ActivitySignupBinding
import com.dicoding.midsubmissionintermediate.helper.ViewModelFactory

class SignupActivity : AppCompatActivity() {
    private val viewModel by viewModels<SignupViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        playAnimation()
        setupAction()
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

    private fun setupAction() {
        with(binding){
            buttonSignup.setOnClickListener {
                val name = edRegisterName.text.toString()
                val email = edRegisterEmail.text.toString()
                val password = edRegisterPassword.text.toString()

                viewModel.postDataRegister(name, email, password)
            }
        }
        viewModel.showLoading.observe(this){
            showLoading(it)
        }
        viewModel.registerSuccess.observe(this){ isSuccess->
            isRegisterSuccess(isSuccess)
        }
    }
    private fun isRegisterSuccess(isSuccess: Boolean){

        if (isSuccess){
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.success))
                setMessage(getString(R.string.regist_success_msg))
                setPositiveButton(getString(R.string.next)) { _, _ ->
                    finish()
                }
                create()
                show()
            }
        } else {
            AlertDialog.Builder(this).apply {
                setTitle(getString(R.string.failed))
                setMessage(getString(R.string.regist_failed_msg))
                setNegativeButton(getString(R.string.back)) { _, _ ->
                    return@setNegativeButton
                }
                create()
                show()
            }
        }
    }
    private fun playAnimation() {
        with(binding){
            ObjectAnimator.ofFloat(ivRegister, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

            val title = ObjectAnimator.ofFloat(tvRegisterTitle, View.ALPHA, 1f).setDuration(230)
            val tvName = ObjectAnimator.ofFloat(tvRegisterName, View.ALPHA, 1f).setDuration(230)
            val etName = ObjectAnimator.ofFloat(edRegisterName, View.ALPHA, 1f).setDuration(230)
            val tvEmail = ObjectAnimator.ofFloat(tvRegisterEmail, View.ALPHA, 1f).setDuration(230)
            val etEmail = ObjectAnimator.ofFloat(edRegisterEmail, View.ALPHA, 1f).setDuration(230)
            val tvPassword = ObjectAnimator.ofFloat(tvRegisterPassword, View.ALPHA, 1f).setDuration(230)
            val etPassword = ObjectAnimator.ofFloat(edRegisterPassword, View.ALPHA, 1f).setDuration(230)
            val signup = ObjectAnimator.ofFloat(buttonSignup, View.ALPHA, 1f).setDuration(230)

            AnimatorSet().apply {
                playSequentially(title, tvName, etName, tvEmail, etEmail, tvPassword, etPassword, signup)
                startDelay = 200
                start()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.pbSignup.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

}