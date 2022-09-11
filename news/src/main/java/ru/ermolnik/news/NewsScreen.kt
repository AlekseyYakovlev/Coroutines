package ru.ermolnik.news

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@Composable
fun NewsScreen(viewModel: NewsViewModel) {
    val state by viewModel.state.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()

    SwipeRefresh(
        state = rememberSwipeRefreshState(isRefreshing),
        onRefresh = { viewModel.onRefresh() }
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                Box {
                    when (state) {
                        is NewsState.Loading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .size(50.dp)
                                    .align(Alignment.Center)
                            )
                        }
                        is NewsState.Error -> {
                            Text(
                                text = stringResource(id = R.string.news__error_text, (state as NewsState.Error).throwable.toString()),
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                        is NewsState.Content -> {
                            Text(
                                text = (state as NewsState.Content).id.toString(),
                                modifier = Modifier
                                    .wrapContentSize()
                                    .align(Alignment.Center),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            if (state is NewsState.Error) {
                item {
                    Button(onClick = { viewModel.onRefresh() }) {
                        Text(
                            text = stringResource(id = R.string.news__retry_text),
                        )
                    }
                }
            }
        }
    }
}
