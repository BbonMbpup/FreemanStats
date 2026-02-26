package com.example.freemanstats.presentation.settingspage

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.freemanstats.data.ClanPreferencesImpl
import com.example.freemanstats.databinding.FragmentSettingsBinding
import com.example.freemanstats.presentation.startpage.LaunchActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment() {

    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadCurrentSettings()
        setupListeners()
    }

    private fun loadCurrentSettings() {
        val currentTag = ClanPreferencesImpl.getClanTag(requireContext())
        binding.currentClanTagTextView.text = currentTag ?: "Не установлен"
    }

    private fun setupListeners() {
        binding.changeClanTagButton.setOnClickListener {
            val newTag = binding.newClanTagEditText.text.toString().trim()
            if (newTag.isNotEmpty()) {
                changeClanTag(newTag)
            } else {
                showMessage("Введите новый тег клана")
            }
        }

        binding.resetClanTagButton.setOnClickListener {
            showResetConfirmationDialog()
        }

        binding.openLaunchActivityButton.setOnClickListener {
            openLaunchActivity()
        }
    }

    private fun changeClanTag(newTag: String) {
        if (!isValidClanTag(newTag)) {
            showMessage("Неверный формат тега. Должен начинаться с #")
            return
        }

        try {
            ClanPreferencesImpl.saveClanTag(requireContext(), newTag)
            binding.currentClanTagTextView.text = newTag
            binding.newClanTagEditText.text?.clear()
            showMessage("Тег клана сохранен")
        } catch (e: Exception) {
            showMessage("Ошибка сохранения: ${e.message}")
        }
    }

    private fun showResetConfirmationDialog() {
        AlertDialog.Builder(requireContext())
            .setTitle("Сброс тега клана")
            .setMessage("Вы уверены, что хотите сбросить тег клана? Приложение перезапустится.")
            .setPositiveButton("Сбросить") { dialog, which ->
                resetClanTag()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }

    private fun resetClanTag() {
        ClanPreferencesImpl.saveClanTag(requireContext(), "")
        restartApp()
    }

    private fun openLaunchActivity() {
        startActivity(Intent(requireActivity(), LaunchActivity::class.java))
        requireActivity().finish()
    }

    private fun restartApp() {
        val intent = Intent(requireContext(), LaunchActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    private fun isValidClanTag(tag: String): Boolean {
        return tag.startsWith("#") && tag.length in 4..15
    }

    private fun showMessage(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}