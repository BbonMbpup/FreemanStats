package com.example.freemanstats.presentation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.freemanstats.api.RetrofitClient
import com.example.freemanstats.data.ClanWarRepository
import com.example.freemanstats.databinding.FragmentCurrentWarBinding
import com.example.freemanstats.model.ClanWar
import com.google.android.material.appbar.AppBarLayout
import kotlin.math.abs
import kotlin.math.max

class CurrentWarFragment : Fragment() {
    private lateinit var binding: FragmentCurrentWarBinding
    private lateinit var viewModel: CurrentWarViewModel
    private lateinit var adapter: MemberAdapter
    private val clanTag = "#2G082QRJL"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("Fragment", "onCreateView Start")
        binding = FragmentCurrentWarBinding.inflate(inflater, container, false)
        Log.d("Fragment", "onCreateView Finish")
        val context = requireContext()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("Fragment", "onViewCreated start")
        // Инициализация ViewModel

        val api = RetrofitClient.clashOfClansApi
        val repository = ClanWarRepository(api, requireContext())
        val factory = CurrentWarViewModelFactory(repository)
        Log.d("Fragment", "start init viewModel")
        viewModel = ViewModelProvider(this, factory).get(CurrentWarViewModel::class.java)
        Log.d("Fragment", "${viewModel.currentWar}")
        // Настройка RecyclerView
        adapter = MemberAdapter(emptyList(), emptyList())
        binding.recyclerViewPlayers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerViewPlayers.adapter = adapter
        Log.d("Fragment", "finish set adapter rv")

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

    private fun loadImage(url: String, imageView: ImageView) {
        // Используйте Glide, Picasso или другой библиотеку для загрузки изображений
        Glide.with(this)
            .load(url)
            .into(imageView)
    }
}