package com.pahnal.mystoryapp.presentation.auth_features.register

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.pahnal.mystoryapp.R
import com.pahnal.mystoryapp.databinding.ActivityRegisterBinding
import com.pahnal.mystoryapp.utils.ViewModelFactory
import com.pahnal.mystoryapp.utils.goBack
import com.pahnal.mystoryapp.utils.hideKeyboard
import com.pahnal.mystoryapp.utils.toast

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by lazy {
        val factory = ViewModelFactory.getInstance(application)
        ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUI()
    }

    private fun setupUI() {
        initObservers()
        with(binding) {
            btnGoLogin.setOnClickListener { goBack() }
            btnRegister.setOnClickListener { register() }
        }

    }

    private fun initObservers() {
        with(binding) {
            viewModel.isLoading.observe(this@RegisterActivity) { isLoading ->
                btnRegister.isEnabled = !isLoading
                btnRegister.text =
                    if (isLoading) getString(R.string.loading) else getString(R.string.register)
            }
            viewModel.errorText.observe(this@RegisterActivity) { text ->
                if (text.isNotEmpty()) {
                    edRegisterPassword.text?.clear()
                    toast(text)
                }
            }
            viewModel.successText.observe(this@RegisterActivity) { text ->
                if (text.isNotEmpty()) {
                    this@RegisterActivity.goBack()
                    toast(text)
                }
            }
        }

    }

    private fun register() {
        with(binding) {
            val name: String = edRegisterName.text.toString()
            val email: String = edRegisterEmail.text?.trim().toString()
            val password: String = edRegisterPassword.text?.trim().toString()

            validationRegister(name, email, password) {
                this@RegisterActivity.hideKeyboard()
                viewModel.register(name, email, password)
            }
        }

    }

    private fun validationRegister(
        name: String,
        email: String,
        password: String,
        success: () -> Unit
    ) {
        val nameRequiredText = getString(R.string.is_required, getString(R.string.name))
        val emailRequiredText = getString(R.string.is_required, getString(R.string.email))
        val passwordRequiredText = getString(R.string.is_required, getString(R.string.password))
        with(binding) {
            when {
                name.trim().isEmpty() && email.isEmpty() && password.isEmpty() -> {
                    edRegisterName.error = nameRequiredText
                    edRegisterEmail.error = emailRequiredText
                    edRegisterPassword.error = passwordRequiredText
                }
                email.isEmpty() && password.isEmpty() -> {
                    edRegisterEmail.error = emailRequiredText
                    edRegisterPassword.error = passwordRequiredText
                }
                name.trim().isEmpty() && password.isEmpty() -> {
                    edRegisterName.error = nameRequiredText
                    edRegisterPassword.error = passwordRequiredText
                }
                name.trim().isEmpty() && email.isEmpty() -> {
                    edRegisterName.error = nameRequiredText
                    edRegisterEmail.error = emailRequiredText
                }
                name.trim().isEmpty() -> edRegisterName.error = nameRequiredText
                email.isEmpty() -> edRegisterEmail.error = emailRequiredText
                password.isEmpty() -> edRegisterPassword.error = passwordRequiredText
                else -> success()
            }
        }

    }
}