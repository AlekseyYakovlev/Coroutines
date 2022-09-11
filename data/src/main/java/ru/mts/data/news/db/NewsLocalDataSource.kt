package ru.mts.data.news.db

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import ru.mts.data.main.AppDatabase
import ru.mts.data.utils.runOperationCatching

class NewsLocalDataSource(private val context: Context) {

    fun getNewsFlow(): Flow<NewsEntity?>
            = AppDatabase.getDatabase(context).newsDao().getLastFlow()

    suspend fun saveNews(newEntry: NewsEntity) =
        runOperationCatching {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).newsDao().insert(newEntry)
            }
        }

    suspend fun cleanNews() =
        runOperationCatching {
            withContext(Dispatchers.IO) {
                AppDatabase.getDatabase(context).newsDao().clean()
            }
        }
}
