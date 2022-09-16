package ru.ermolnik.news

sealed class NewsState {
    data class Loading(val id: Int? = null) : NewsState()
    data class Error(val throwable: Throwable) : NewsState()
    data class Content(val id: Int) : NewsState()
}