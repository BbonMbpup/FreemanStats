package com.example.freemanstats.presentation

import android.os.Bundle
import android.util.Log
import android.view.inputmethod.InputBinding
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freemanstats.R
import com.example.freemanstats.animation.ZoomOutPageTransformer
import com.example.freemanstats.api.RetrofitClient
import com.example.freemanstats.databinding.ActivityMainBinding
import com.example.freemanstats.model.ClanWar
import com.google.android.material.tabs.TabLayoutMediator
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Настройка ViewPager2 и TabLayout
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        // Создаем адаптер для ViewPager2
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Связываем TabLayout с ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Текущая война"
                1 -> "Статистика"
                2 -> "История"
                else -> null
            }
            tab.icon = when (position) {
                0 -> ContextCompat.getDrawable(this, R.drawable.swords_tab)
                1 -> ContextCompat.getDrawable(this, R.drawable.table_tab)
                2 -> ContextCompat.getDrawable(this, R.drawable.history_tab)
                else -> null
            }
        }.attach()
        viewPager.setPageTransformer(ZoomOutPageTransformer())
    }
}