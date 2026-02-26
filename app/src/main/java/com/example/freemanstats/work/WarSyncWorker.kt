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
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withTimeout
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.coroutines.cancellation.CancellationException

@HiltWorker
class WarSyncWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val params: WorkerParameters,
    private val repository: ClanWarRepository
) : CoroutineWorker(context, params) {

    init {
        Log.d("WarSyncWorker", "Конструктор WarSyncWorker вызван")
    }

    override suspend fun doWork(): Result {
        Log.d("WarSyncWorker", "Запущен doWork()")
        val isManualTest = tags.contains("ManualWarTest")

        return try {
            val clanTag = "#2GLCJUQC2"

            // Добавляем таймаут для сетевого запроса
            val war = withTimeout(30_000) {
                repository.getCurrentWar(clanTag)
            }

            // Если войны нет
            if (war == null) {
                Log.d("WarSyncWorker", "Война не найдена")
                if (isManualTest) {
                    showNotification("Нет текущей войны", "Пока что война не началась.")
                }
                return Result.success()
            }

            val endTimeStr = war.endTime ?: return Result.success()
            val endTime = parseEndTime(endTimeStr)
            val now = System.currentTimeMillis()

            if (isManualTest) {
                val formattedTime = SimpleDateFormat("HH:mm dd MMM yyyy ", Locale.getDefault())
                    .format(Date(endTime))
                showNotification("Текущая война", "Завершается в $formattedTime")
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
                        // TODO: Добавьте здесь сохранение данных если нужно
                    } else {
                        Log.d("WarSyncWorker", "Война еще не завершилась")
                    }
                } else {
                    Log.d("WarSyncWorker", "Война в состоянии ${war.state}")
                    repository.saveWarResultToDatabase(war)
                    showNotification("Война завершена!", "Данные о войне успешно сохранены.")
                }
            }

            Result.success()

        } catch (e: TimeoutCancellationException) {
            Log.e("WarSyncWorker", "Таймаут запроса войны: ${e.message}")
            Result.retry() // Попробуем again later
        } catch (e: CancellationException) {
            Log.e("WarSyncWorker", "Работа отменена: ${e.message}")
            Result.success() // Не пытаемся again при отмене
        } catch (e: Exception) {
            Log.e("WarSyncWorker", "Общая ошибка: ${e.message}")
            Result.retry() // Попробуем again при других ошибках
        }
    }

    private fun showNotification(title: String, message: String) {
        NotificationUtils.createNotificationChannel(applicationContext)
        NotificationUtils.showNotification(applicationContext, title, message)
    }

    private fun parseEndTime(endTimeStr: String): Long {
        val format = SimpleDateFormat("yyyyMMdd'T'HHmmss.SSSX", Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        return format.parse(endTimeStr)?.time ?: 0L
    }
}