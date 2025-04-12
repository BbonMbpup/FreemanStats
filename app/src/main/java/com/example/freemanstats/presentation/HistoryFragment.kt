package com.example.freemanstats.presentation

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemanstats.api.RetrofitClient
import com.example.freemanstats.data.ClanWarRepository
import com.example.freemanstats.data.SecurePrefs
import com.example.freemanstats.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: WarLogAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val api = RetrofitClient.clashOfClansApi
        val repository = ClanWarRepository(api, requireContext())
        viewModel = ViewModelProvider(
            this,
            HistoryViewModelFactory(repository)
        ).get(HistoryViewModel::class.java)

        // Настройка RecyclerView
        adapter = WarLogAdapter()
        binding.recyclerViewHistory.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
        }
        setupObservers()
        loadWarHistory()
    }

    private fun setupObservers() {
        viewModel.warLogs.observe(viewLifecycleOwner) { logs ->
            logs?.let {
                if (it.isEmpty()) {
                    showError("Нет данных о войнах")
                } else {
                    adapter.submitList(it)
                    showContent()
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let { showError(it) }
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) { error ->
            error?.let {
                binding.errorTextView.text = it
                binding.errorTextView.visibility = View.VISIBLE
                binding.recyclerViewHistory.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
            }
        }
    }

    private fun loadWarHistory() {

        viewModel.loadHistory(CLAN_TAG)
    }

    private fun showError(message: String) {
        binding.errorTextView.text = message
        binding.errorTextView.visibility = View.VISIBLE
        binding.recyclerViewHistory.visibility = View.GONE
    }

    private fun showContent() {
        binding.errorTextView.visibility = View.GONE
        binding.recyclerViewHistory.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private val CLAN_TAG = "#2G082QRJL"
    }
}