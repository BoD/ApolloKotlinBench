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
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.sql.SqlNormalizedCacheFactory
import com.example.myapplication.ui.theme.MyApplicationTheme
import kotlin.concurrent.thread
import kotlin.time.measureTime

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
                                    var apolloTests = ApolloTests(this@MainActivity, null)

                                    // No cache
                                    Log.d("App", "----------------------------")
                                    Log.d("App", "NoCache / tracksById")
                                    measureRepeated {
                                        apolloTests.tracksById_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "NoCache / tracksByIdWithFragments")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_NetworkOnly()
                                    }


                                    // Memory cache
                                    apolloTests = ApolloTests(this@MainActivity, MemoryCacheFactory())

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory / tracksById / NetworkOnly")
                                    measureRepeated {
                                        apolloTests.tracksById_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory / tracksById / CacheOnly")
                                    measureRepeated {
                                        apolloTests.tracksById_CacheOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory / tracksByIdWithFragments / NetworkOnly")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory / tracksByIdWithFragments / CacheOnly")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_CacheOnly()
                                    }


                                    // Sql cache
                                    apolloTests = ApolloTests(this@MainActivity, SqlNormalizedCacheFactory())

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Sql / tracksById / NetworkOnly")
                                    measureRepeated {
                                        apolloTests.tracksById_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Sql / tracksById / CacheOnly")
                                    measureRepeated {
                                        apolloTests.tracksById_CacheOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Sql / tracksByIdWithFragments / NetworkOnly")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Sql / tracksByIdWithFragments / CacheOnly")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_CacheOnly()
                                    }


                                    // Memory then Sql cache
                                    apolloTests = ApolloTests(this@MainActivity, MemoryCacheFactory().chain(SqlNormalizedCacheFactory()))

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory then Sql / tracksById / NetworkOnly")
                                    measureRepeated {
                                        apolloTests.tracksById_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory then Sql / tracksById / CacheOnly")
                                    measureRepeated {
                                        apolloTests.tracksById_CacheOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory then Sql / tracksByIdWithFragments / NetworkOnly")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_NetworkOnly()
                                    }

                                    Log.d("App", "----------------------------")
                                    Log.d("App", "Memory then Sql / tracksByIdWithFragments / CacheOnly")
                                    measureRepeated {
                                        apolloTests.tracksByIdWithFragments_CacheOnly()
                                    }
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

private const val WORK_LOAD = 10

fun measureRepeated(block: () -> Unit) {
    // Run once first to warm up
    block()

    val duration = measureTime {
        repeat(WORK_LOAD) {
            block()
        }
    }
    val average = duration / WORK_LOAD
    Log.d("App", "Average time: ${average.inWholeMilliseconds}ms")
}
