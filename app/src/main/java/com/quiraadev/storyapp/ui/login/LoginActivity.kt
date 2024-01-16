package com.quiraadev.storyapp.ui.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.chibatching.kotpref.Kotpref
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.data.factory.ViewModelFactory
import com.quiraadev.storyapp.databinding.ActivityLoginBinding
import com.quiraadev.storyapp.ui.register.RegisterActivity
import com.quiraadev.storyapp.ui.story.StoryActivity
import com.quiraadev.storyapp.utils.DialogType

class LoginActivity : AppCompatActivity(R.layout.activity_login) {
    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this, ViewModelFactory.getInstance(application)).get(LoginViewModel::class.java)
    }

    private val binding by viewBinding(ActivityLoginBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Kotpref.init(this)

        showLoading(false)

        binding.textButtonRegister.setOnClickListener {
            startActivity(
                Intent(
                    this@LoginActivity,
                    RegisterActivity::class.java
                )
            )
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.edEmail.text.toString()
            val password = binding.edPassword.text.toString()

            if (email.isEmpty() || password.isEmpty()) Toast.makeText(
                this,
                "All fields must be filled",
                Toast.LENGTH_SHORT
            ).show()
            else viewModel.login(email, password)
        }

        viewModel.loginState.observe(this) { state ->
            when (state) {
                is LoginState.Error -> {
                    createDialog(DialogType.ERROR, state.message)
                    showLoading(false)
                }

                is LoginState.Loading -> showLoading(true)
                is LoginState.Success -> {
                    showLoading(false)
                    createDialog(DialogType.SUCCESS, "Succesful")
                }
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
                startActivity(Intent(this@LoginActivity, StoryActivity::class.java))
                finish()
            }
            .create()
    }


    private fun showLoading(isLoading: Boolean) {
        binding.progressCircular.isVisible = isLoading
    }

}