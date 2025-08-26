package com.example.freemanstats.presentation.currentwarpage

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freemanstats.domain.repository.ClanWarRepository
import com.example.freemanstats.databinding.FragmentCurrentWarBinding
import com.example.freemanstats.domain.model.ClanWar
import com.google.android.material.appbar.AppBarLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.max

@AndroidEntryPoint
class CurrentWarFragment : Fragment() {

    private lateinit var binding: FragmentCurrentWarBinding
    private lateinit var adapter: MemberAdapter
    private val clanTag = "#2GLCJUQC2"

    private val viewModel: CurrentWarViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentWarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Fragment", "onViewCreated start")

        // Настройка RecyclerView
        adapter = MemberAdapter(emptyList(), emptyList())
        binding.recyclerViewPlayers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPlayers.adapter = adapter
        binding.recyclerViewPlayers.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val canScrollUp = recyclerView.canScrollVertically(-1)
                binding.swipeRefreshLayout.isEnabled = !canScrollUp
            }
        })
        Log.d("Fragment", "finish set adapter rv")
        binding.swipeRefreshLayout.setOnRefreshListener {
            Log.d("SwipeRefresh", "Пользователь обновил экран")
            refreshData()
        }

        viewModel.loadCurrentWar(clanTag)
        setupObservers()
    }

    private fun setupObservers() {

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // Наблюдение за данными
        viewModel.currentWar.observe(viewLifecycleOwner) { clanWar ->
            if (clanWar != null) {
                updateWarInfo(clanWar)
                val members = clanWar.clan.members ?: emptyList()
                val opponentMembers = clanWar.opponent.members ?: emptyList()
                adapter.updateData(members, opponentMembers)
                binding.appBarLayout.visibility = View.VISIBLE
                binding.recyclerViewPlayers.visibility = View.VISIBLE

            } else {
                binding.errorTextView.text = "Данные о войне отсутствуют"
                binding.errorTextView.visibility = View.VISIBLE
                binding.recyclerViewPlayers.visibility = View.GONE
            }
        }

        // Наблюдение за ошибками
        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                binding.errorTextView.text = error
                binding.errorTextView.visibility = View.VISIBLE
                binding.appBarLayout.visibility = View.GONE
                binding.recyclerViewPlayers.visibility = View.GONE
                Log.d("Fragment", "Error: ${error}")
            } else {
                binding.errorTextView.visibility = View.GONE
            }
        }

        binding.appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset ->
            val scrollRange = appBarLayout.totalScrollRange
            val offset = abs(verticalOffset)
            val scale = 1 - (offset.toFloat() / scrollRange.toFloat())

            // Ограничиваем масштаб эмблем до 75%
            val badgeScale = max(0.75f, scale)

            // Уменьшаем размер эмблем
            binding.clanBadgeImageView.scaleX = badgeScale
            binding.clanBadgeImageView.scaleY = badgeScale
            binding.opponentBadgeImageView.scaleX = badgeScale
            binding.opponentBadgeImageView.scaleY = badgeScale
        })
    }

    private fun updateWarInfo(clanWar: ClanWar) {

        // Устанавливаем гербы кланов
        loadImage(clanWar.clan.badgeUrls.medium, binding.clanBadgeImageView)
        loadImage(clanWar.opponent.badgeUrls.medium, binding.opponentBadgeImageView)

        // Устанавливаем названия кланов
        binding.clanNameTextView.text = clanWar.clan.name
        binding.opponentNameTextView.text = clanWar.opponent.name

        // Устанавливаем общее количество звезд
        val totalClanStars = clanWar.clan.stars
        val totalOpponentStars = clanWar.opponent.stars
        binding.totalStarsScoreTextView.text = "$totalClanStars-$totalOpponentStars"

        // Устанавливаем процент разрушения
        val destructionPercentage = clanWar.clan.destructionPercentage
        binding.destructionPercentageTextView.text = "💥 ${destructionPercentage.toInt()}%"

        // Устанавливаем использованные атаки / общее количество атак
        val attacksUsed = clanWar.clan.attacks
        val totalAttacks = clanWar.teamSize * 2
        binding.attacksUsedTextView.text = "⚔️ $attacksUsed/$totalAttacks"
    }

    private fun refreshData() {
        // Показываем значок обновления
        binding.swipeRefreshLayout.isRefreshing = true

        viewModel.loadCurrentWar(clanTag)

        // Таймер/наблюдатель — чтобы выключить индикатор после загрузки
        viewModel.currentWar.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
        }

        viewModel.error.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun loadImage(url: String, imageView: ImageView) {
        // Используйте Glide, Picasso или другой библиотеку для загрузки изображений
        Glide.with(this)
            .load(url)
            .into(imageView)
    }
}