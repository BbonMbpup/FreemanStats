package com.example.freemanstats.presentation.startpage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.freemanstats.data.ClanPreferences
import com.example.freemanstats.databinding.ActivityLaunchBinding
import com.example.freemanstats.presentation.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding
    private val viewModel: LaunchViewModel by viewModels()

    @Inject
    lateinit var clanPreferences: ClanPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (clanPreferences.getClanTag() != null) {
            // Переходим сразу в MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Проверяем, может тег уже сохранен
        checkIfClanTagExists()
        setupObservers()
        setupListeners()
    }

    private fun checkIfClanTagExists() {
        if (viewModel.hasClanTag()) {
            navigateToMainActivity()
        }
    }

    private fun setupObservers() {
        viewModel.navigationEvent.observe(this) { shouldNavigate ->
            if (shouldNavigate) {
                navigateToMainActivity()
            }
        }

        viewModel.error.observe(this) { error ->
            if (error != null) {
                binding.errorTextView.text = error
                binding.errorTextView.visibility = View.VISIBLE
            } else {
                binding.errorTextView.visibility = View.GONE
            }
        }
    }

    private fun setupListeners() {
        binding.saveButton.setOnClickListener {
            val clanTag = binding.clanTagEditText.text.toString().trim()
            if (clanTag.isNotEmpty()) {
                viewModel.saveClanTag(clanTag)
            } else {
                binding.errorTextView.text = "Введите тег клана"
                binding.errorTextView.visibility = View.VISIBLE
            }
        }
    }

    private fun navigateToMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}