package com.quiraadev.storyapp.ui.register

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityRegisterBinding
import com.quiraadev.storyapp.ui.login.LoginActivity
import com.quiraadev.storyapp.utils.DialogType


class RegisterActivity : AppCompatActivity(R.layout.activity_register) {
    private val viewModel: RegisterViewModel by lazy {
        ViewModelProvider(
            this,
            ViewModelFactory.getInstance(application)
        ).get(RegisterViewModel::class.java)
    }

    private val binding by viewBinding(ActivityRegisterBinding::bind)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = binding.edUsername.text.toString()
        val email = binding.edEmail.text.toString()
        val password = binding.edPassword.text.toString()

        binding.btnRegister.setOnClickListener {
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) Toast.makeText(
                this,
                "All fields must be filled",
                Toast.LENGTH_SHORT
            ).show()
            else viewModel.register(username, email, password)
        }

        binding.textButtonLogin.setOnClickListener {
            startActivity(
                Intent(
                    this@RegisterActivity,
                    LoginActivity::class.java
                )
            )
        }

        viewModel.registerState.observe(this) { state ->
            when(state) {
                is RegisterState.Error -> {
                    createDialog(DialogType.ERROR, state.message)
                    showLoading(false)
                }
                is RegisterState.Loading -> showLoading(true)
                is RegisterState.Success -> {
                    createDialog(DialogType.SUCCESS, "Successful")
                    showLoading(false)
                }
                null -> { }
            }
        }
    }

    private fun createDialog(dialogType: DialogType, message: String?) {
        val dialog = when (dialogType) {
            DialogType.SUCCESS -> showSuccessDialog(message)
            DialogType.ERROR -> showErrorDialog(message)
        }
        dialog.show()
    }

    private fun showErrorDialog(message: String?): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Error")
            .setMessage(message ?: "An Error Occured")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }

    private fun showSuccessDialog(message: String?): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Success")
            .setMessage(message ?: "Operation Successful")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                finish()
            }
            .create()
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressCircular.isVisible = isLoading
    }
}