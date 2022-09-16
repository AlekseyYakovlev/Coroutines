package ru.ermolnik.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NewsScreen(
    screenState: NewsState,
    onRefresh: () -> Unit,
) {
    SwipeRefresh(
        state = rememberSwipeRefreshState(screenState is NewsState.Loading),
        onRefresh = onRefresh,
    ) {
        when (screenState) {
            is NewsState.Loading -> {
                Box(modifier = Modifier.fillMaxSize()) {
                    (screenState.id?.toString()
                        ?: stringResource(id = R.string.news__is_loading_text))
                        .let {
                            Text(
                                text = it,
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                }
            }
            is NewsState.Content -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Text(
                            text = screenState.id.toString(),
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                }
            }
            is NewsState.Error -> {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Text(
                            text = stringResource(
                                id = R.string.news__error_text,
                                screenState.throwable.toString()
                            ),
                            modifier = Modifier.wrapContentSize()
                        )
                    }
                    item {
                        Button(onClick = onRefresh) {
                            Text(
                                text = stringResource(id = R.string.news__retry_text),
                            )
                        }
                    }
                }
            }
        }
    }
}