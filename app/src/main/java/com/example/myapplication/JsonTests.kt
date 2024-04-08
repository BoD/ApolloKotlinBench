package com.example.myapplication

import android.content.Context
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.mockserver.MockRequest
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.mockserver.MockServerHandler
import com.apollographql.apollo3.tracks.GetTracksByIdsQuery
import kotlinx.coroutines.runBlocking

object JsonTests {
    private var mockServer: MockServer? = null

    private fun getMockServer(context: Context): MockServer {
        if (mockServer == null) {
            mockServer = MockServer(
                object : MockServerHandler {
                    private val mockResponse = MockResponse.Builder()
                        .statusCode(200)
                        .body(Utils.resource(context, R.raw.tracks_response).readByteString())
                        // Add a delay to simulate network latency
                        .delayMillis(500)
                        .build()

                    override fun handle(request: MockRequest): MockResponse {
                        return mockResponse
                    }
                }
            )
        }
        return mockServer!!
    }

    fun queryTracks(context: Context) {
        val serverUrl = runBlocking {
            // On a device, the URL will be something like "http://:::1234", not sure why
            getMockServer(context).url().replace("::", "localhost")
        }
        val client = ApolloClient.Builder()
            .serverUrl(serverUrl)
            .build()

        runBlocking {
            val response = client.query(GetTracksByIdsQuery(emptyList())).execute()
            try {
                Log.d("App", "Size=${response.dataAssertNoErrors.tracksByIds.size}")
            } catch (e: Exception) {
                Log.e("App", "Error", e)
            }
        }
    }
}
