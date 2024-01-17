package com.quiraadev.storyapp.ui.setting

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.databinding.ActivitySettingBinding
import com.quiraadev.storyapp.ui.login.LoginActivity

class SettingActivity : AppCompatActivity(R.layout.activity_setting) {
    private val binding by viewBinding(ActivitySettingBinding::bind)
    private val viewModel: SettingViewModel by lazy {
        ViewModelProvider(this).get(SettingViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarSetting)

        binding.toolbarSetting.setNavigationOnClickListener { finish() }

        binding.actionLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }

        binding.actionLogout.setOnClickListener {
            val dialog = confirmationDialog()
            dialog.show()
        }

        viewModel.isLoggedIn.observe(this) { isLoggedIn ->
            if (isLoggedIn == false) {
                startActivity(Intent(this, LoginActivity::class.java))
                finishAffinity()
            }
        }

    }

    private fun confirmationDialog(): AlertDialog {
        return AlertDialog.Builder(this)
            .setTitle("Logout?")
            .setMessage("Next time you have to login again")
            .setPositiveButton("Yes") { dialog, _ ->
                viewModel.logout()
                dialog.dismiss()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
    }
}