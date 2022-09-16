package ru.ermolnik.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import ru.mts.data.news.repository.NewsRepository
import ru.mts.data.utils.doOnError
import ru.mts.data.utils.doOnSuccess

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {
    private val _state = MutableStateFlow<NewsState>(NewsState.Loading())
    val state: StateFlow<NewsState> = _state.asStateFlow()
    private var lastValue: Int? = null

    init {
        combine(
            repository.newsFlow,
            repository.isLoadingFlow,
        ) { news, isLoading ->
            if (isLoading) {
                _state.value = NewsState.Loading(lastValue)
            } else {
                news.doOnError { error ->
                    _state.value = NewsState.Error(error)
                }.doOnSuccess { newsItem ->
                    lastValue = newsItem.id
                    _state.value = NewsState.Content(newsItem.id)
                }
            }
        }.launchIn(viewModelScope)
    }

    fun onRefresh() {
        viewModelScope.launch {
            repository.updateNews().doOnError { error ->
                _state.value = NewsState.Error(error)
            }
        }
    }
}

class NewsViewModelFactory(private val repository: NewsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(NewsRepository::class.java).newInstance(repository)
    }
}
