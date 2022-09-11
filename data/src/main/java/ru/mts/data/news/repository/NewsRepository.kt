package ru.mts.data.news.repository

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapLatest
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.db.toDomain
import ru.mts.data.news.db.toNewsEntity
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.remote.toDomain
import ru.mts.data.utils.Result
import ru.mts.data.utils.doOnSuccess
import ru.mts.data.utils.mapSuccess

class NewsRepository(
    private val newsLocalDataSource: NewsLocalDataSource,
    private val newsRemoteDataSource: NewsRemoteDataSource
) {
    private val _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow = _isLoadingFlow.asStateFlow()

    suspend fun updateNews(): Result<News, Throwable> {
        _isLoadingFlow.value = true
        val res = newsRemoteDataSource.getNews()
            .mapSuccess { it.toDomain() }
            .doOnSuccess {
                newsLocalDataSource.cleanNews()
                newsLocalDataSource.saveNews(it.toNewsEntity())
            }
        _isLoadingFlow.value = false
        return res
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getNews(): Flow<Result<News, Throwable>> {
        return newsLocalDataSource.getNewsFlow()
            .mapLatest { newsEntities ->
                newsEntities
                    ?.let { Result.Success(it.toDomain()) }
                    ?: updateNews()
            }
    }
}
