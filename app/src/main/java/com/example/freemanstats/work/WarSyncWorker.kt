package com.example.freemanstats.work

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.freemanstats.domain.repository.ClanWarRepository
import com.example.freemanstats.utils.NotificationUtils
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone

@HiltWorker
class WarSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: ClanWarRepository
) : CoroutineWorker(context, params) {

    init {
        Log.d("WarSyncWorker", "Конструктор WarSyncWorker вызван")
    }

    override suspend fun doWork(): Result = coroutineScope {
        Log.d("WarSyncWorker", "Запущен doWork()")
        val isManualTest = tags.contains("ManualWarTest")

        val clanTag = "#2GLCJUQC2"
        val war = repository.getCurrentWar(clanTag)

        // Если войны нет
        if (war == null) {
            Log.d("WarSyncWorker", "Война не найдена")
            if (isManualTest) {
                NotificationUtils.createNotificationChannel(applicationContext)
                NotificationUtils.showNotification(
                    applicationContext,
                    "Нет текущей войны",
                    "Пока что война не началась."
                )
            }
            return@coroutineScope Result.success()
        }

        val endTimeStr = war.endTime ?: return@coroutineScope Result.success()
        val endTime = parseEndTime(endTimeStr)
        val now = System.currentTimeMillis()

        if (isManualTest) {
            val formattedTime = SimpleDateFormat("HH:mm dd MMM yyyy ", Locale.getDefault())
                .format(Date(endTime))
            NotificationUtils.createNotificationChannel(applicationContext)
            NotificationUtils.showNotification(
                applicationContext,
                "Текущая война",
                "Завершается в $formattedTime"
            )
            repository.saveWarResultToDatabase(war)
            Log.d("WarSyncWorker", "Данные о войне успешно сохранены в бд")
        } else {
            if (war.state == "inWar" || war.state == "preparation") {
                Log.d("WarSyncWorker", "Идет война или подготовка")

                Log.d("WarSyncWorker", "now: $now")
                Log.d("WarSyncWorker", "endTime: $endTime")
                Log.d("WarSyncWorker", "nowStr: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(now))}")
                Log.d("WarSyncWorker", "endTimeStr: ${SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(endTime))}")


                val isEnded = now >= endTime

                if (isEnded) {
                    Log.d("WarSyncWorker", "Война завершилась, сохраняем...")

                } else {
                    Log.d("WarSyncWorker", "НЕ УДАЛОСЬ СОХРАНИТЬ ДАННЫЕ В БД")
                }
            } else {
                Log.d("WarSyncWorker", "Война в состоянии ${war.state}")
                repository.saveWarResultToDatabase(war)

                NotificationUtils.createNotificationChannel(applicationContext)
                NotificationUtils.showNotification(
                    applicationContext,
                    "Война завершена!",
                    "Данные о войне успешно сохранены."
                )

                NotificationUtils.createNotificationChannel(applicationContext)
                NotificationUtils.showNotification(
                    applicationContext,
                    "Сейчас нет войны",
                    "Война в состоянии ${war.state}"
                )
            }
        }

        Result.success()
    }


    private fun parseEndTime(endTimeStr: String): Long {
        val format = SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSX", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.parse(endTimeStr)?.time ?: 0L
    }
}