package com.example.myapplication

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(Modifier.fillMaxSize()) {
                    Box {
                        Button(
                            onClick = {
                                thread {
                                    val now = System.currentTimeMillis()
                                    JsonTests.queryTracks(this@MainActivity)
                                    Log.d("App", "Time: ${System.currentTimeMillis() - now}ms")
                                }
                            }
                        ) {
                            Text("Run")
                        }
                    }
                }
            }
        }
    }
}
