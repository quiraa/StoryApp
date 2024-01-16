package com.quiraadev.storyapp.ui.setting

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import by.kirich1409.viewbindingdelegate.viewBinding
import com.quiraadev.storyapp.R
import com.quiraadev.storyapp.databinding.ActivitySettingBinding

class SettingActivity : AppCompatActivity(R.layout.activity_setting) {
    private val binding by viewBinding(ActivitySettingBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(binding.toolbarSetting)

        binding.toolbarSetting.setNavigationOnClickListener { finish() }

    }
}