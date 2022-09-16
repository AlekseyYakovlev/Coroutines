package ru.mts.coroutines

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import ru.ermolnik.news.NewsScreen
import ru.ermolnik.news.NewsViewModel
import ru.ermolnik.news.NewsViewModelFactory
import ru.mts.coroutines.ui.theme.CoroutinesTheme
import ru.mts.data.news.db.NewsLocalDataSource
import ru.mts.data.news.remote.NewsRemoteDataSource
import ru.mts.data.news.repository.NewsRepository

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<NewsViewModel> {
        NewsViewModelFactory(
            NewsRepository(
                NewsLocalDataSource(applicationContext),
                NewsRemoteDataSource(),
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CoroutinesTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    NewsScreen(
                        screenState = viewModel.state.collectAsState().value,
                        onRefresh = { viewModel.onRefresh() }
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CoroutinesTheme {
        Greeting("Android")
    }
}