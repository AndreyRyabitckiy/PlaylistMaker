package com.example.playlistmaker.settings.presentation.activity

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.databinding.ActivitySettingBinding
import com.example.playlistmaker.settings.presentation.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingActivity : AppCompatActivity() {

    private val viewModel: SettingsViewModel by viewModel<SettingsViewModel>()
    private lateinit var binding: ActivitySettingBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        with(binding) {

            ivToolBar.setOnClickListener {
                finish()
            }

            llShareApp.setOnClickListener {
                viewModel.shareApp()
            }

            llSendSuport.setOnClickListener {
                viewModel.sendToSupport()
            }

            llUserPolic.setOnClickListener {
                viewModel.userPolicy()
            }

            sDayornight.setOnCheckedChangeListener { _, checked ->
                val theme = if (checked) {
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    AppCompatDelegate.MODE_NIGHT_NO
                }
                viewModel.updateTheme(theme)
            }
        }


        viewModel.theme.observe(this) {
            val checked = if (it == AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM) {
                getSystemNightMode()
            } else {
                it == AppCompatDelegate.MODE_NIGHT_YES
            }

            binding.sDayornight.isChecked = checked

            switchTheme(it)
        }
    }

    private fun getSystemNightMode() = resources
        ?.configuration
        ?.uiMode
        ?.and(Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES

    private fun switchTheme(theme: Int) {
        AppCompatDelegate.setDefaultNightMode(
            theme
        )
    }
}