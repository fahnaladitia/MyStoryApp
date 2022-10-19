package com.pahnal.mystoryapp.presentation.auth_features.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ActivityLoginBinding
import com.pahnal.mystoryapp.domain.model.User
import com.pahnal.mystoryapp.presentation.auth_features.register.RegisterActivity
import com.pahnal.mystoryapp.presentation.story_features.home.HomeActivity
import com.pahnal.mystoryapp.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance()
        ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        initObservers()
        with(binding) {
            btnLogin.setOnClickListener { login() }
            btnGoRegister.setOnClickListener { this@LoginActivity.goTo(RegisterActivity::class.java) }
        }
    }

    private fun initObservers() {
        with(binding) {
            viewModel.isLoading.observe(this@LoginActivity) { isLoading ->
                btnLogin.isEnabled = !isLoading
                btnLogin.text =
                    if (isLoading) getString(R.string.loading) else getString(R.string.login)
            }
            viewModel.errorText.observe(this@LoginActivity) { text ->
                if (text.isNotEmpty()) {
                    edLoginPassword.text?.clear()
                    toast(text)
                }
            }
            viewModel.successLogin.observe(this@LoginActivity) { user ->
                if (user != null) {
                    insertUser(user)
                    this@LoginActivity.goTo(HomeActivity::class.java, true)
                    toast(getString(R.string.success_login))
                }
            }
        }
    }

    private fun insertUser(user: User) {
        lifecycleScope.launch(Dispatchers.IO) {
            DataPref.insertUser(user, this@LoginActivity)
        }
    }

    private fun login() {
        with(binding) {
            val email: String = edLoginEmail.text?.trim().toString()
            val password: String = edLoginPassword.text?.trim().toString()

            validationLogin(email, password) {
                this@LoginActivity.hideKeyboard()
                viewModel.login(email, password)
            }
        }
    }

    private fun validationLogin(email: String, password: String, success: () -> Unit) {
        val emailRequiredText = getString(R.string.is_required, getString(R.string.email))
        val passwordRequiredText = getString(R.string.is_required, getString(R.string.password))

        with(binding) {
            when {
                email.isEmpty() && password.isEmpty() -> {
                    edLoginEmail.error = emailRequiredText
                    edLoginPassword.error = passwordRequiredText
                }
                email.isEmpty() -> edLoginEmail.error = emailRequiredText
                password.isEmpty() -> edLoginPassword.error = passwordRequiredText
                else -> success()
            }
        }
    }
}