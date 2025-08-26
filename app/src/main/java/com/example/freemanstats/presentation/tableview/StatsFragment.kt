package com.example.freemanstats.presentation.tableview

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.evrencoskun.tableview.TableView
import com.example.freemanstats.R
import com.example.freemanstats.databinding.FragmentStatsBinding
import com.example.freemanstats.domain.model.PlayerStats
import com.example.freemanstats.presentation.tableview.listeners.StatsTableViewListener
import com.example.freemanstats.work.TestWorker
import com.example.freemanstats.work.WarSyncWorker
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StatsFragment : Fragment() {

    private var _binding: FragmentStatsBinding? = null
    private val binding get() = _binding!!

    private lateinit var tableAdapter: TableAdapter
    private lateinit var tableView: TableView

    private val viewModel: StatsTableViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.periodSlider.setLabelFormatter { value ->
            when (value.toInt()) {
                0 -> "День"
                1 -> "Неделя"
                2 -> "Месяц"
                else -> ""
            }
        }

        tableView = binding.myTableView

        initTableView()
        setupTestButton()
        observeData()
    }

    private fun setupTestButton() {
        binding.btnRefresh.setOnClickListener {
            viewModel.loadStats()

        }
    }

    private fun initTableView() {
        tableAdapter = TableAdapter(requireContext())
        tableView.setAdapter(tableAdapter)
        tableView.tableViewListener = StatsTableViewListener(tableView)
    }

    private fun observeData() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            if (state.isLoading) {
                showLoading()
            } else {
                hideLoading()
                state.stats?.let { statsList ->
                    val rows = state.rowHeaders // список строк таблицы
                    Log.d("StatsFragment", "Данные загружены: ${rows?.size} строк")
                    updateTable(statsList)
                }
            }
        }
    }

    private fun updateTable(stats: List<PlayerStats>) {
        tableAdapter.setUserList(stats)
    }

    private fun showLoading() {
        binding.progressBar.visibility = View.VISIBLE
        tableView.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        binding.progressBar.visibility = View.GONE
        tableView.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}