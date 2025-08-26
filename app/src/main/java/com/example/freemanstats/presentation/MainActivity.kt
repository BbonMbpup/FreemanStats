package com.example.freemanstats.presentation

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.freemanstats.R
import com.example.freemanstats.animation.ZoomOutPageTransformer
import com.example.freemanstats.databinding.ActivityMainBinding
import com.example.freemanstats.utils.CoCTagGenerator
import com.example.freemanstats.work.TestWorker
import com.example.freemanstats.work.WarSyncWorker
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.Executors

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        askNotificationPermission()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupViewPager()
        setupTestButton()
    }



    private fun setupTestButton() {
        findViewById<Button>(R.id.buttonTestWorker).setOnClickListener {
            Log.d("WarSyncWorker", "Запускаем ручной WarSyncWorker")
            val request = OneTimeWorkRequestBuilder<WarSyncWorker>()
                .addTag("ManualWarTest")
                .build()
            WorkManager.getInstance(this).enqueue(request)
            val testRequest = OneTimeWorkRequestBuilder<TestWorker>()
                .addTag("ManualTestWorker")
                .build()
            WorkManager.getInstance(this).enqueue(testRequest)
        }
    }

    private fun setupViewPager() {
        // Настройка ViewPager и TabLayout
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout

        // Создаем адаптер для ViewPager
        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter

        // Связываем TabLayout с ViewPager
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

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (!isGranted) {
            Toast.makeText(this, "Разрешите уведомления в настройках", Toast.LENGTH_LONG).show()
        }
    }

    private fun askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}