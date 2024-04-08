package com.example.myapplication

import android.content.Context
import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.Query
import com.apollographql.apollo3.cache.normalized.ApolloStore
import com.apollographql.apollo3.cache.normalized.FetchPolicy
import com.apollographql.apollo3.cache.normalized.api.MemoryCacheFactory
import com.apollographql.apollo3.cache.normalized.api.NormalizedCacheFactory
import com.apollographql.apollo3.cache.normalized.fetchPolicy
import com.apollographql.apollo3.cache.normalized.store
import com.apollographql.apollo3.mockserver.MockRequest
import com.apollographql.apollo3.mockserver.MockResponse
import com.apollographql.apollo3.mockserver.MockServer
import com.apollographql.apollo3.mockserver.MockServerHandler
import com.apollographql.apollo3.tracks.GetTracksByIdsQuery
import com.apollographql.apollo3.tracks.GetTracksByIdsWithFragmentsQuery
import kotlinx.coroutines.runBlocking

class ApolloTests(
    private val context: Context,
    private val cacheFactory: NormalizedCacheFactory?
) {
    private val mockServer: MockServer = MockServer(
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

    private val memoryCacheFactory = MemoryCacheFactory()

    private val apolloClient: ApolloClient = run {
        val serverUrl = runBlocking {
            // On a device, the URL will be something like "http://:::1234", not sure why
            mockServer.url().replace("::", "localhost")
        }
        ApolloClient.Builder()
            .serverUrl(serverUrl)
            .let {
                if (cacheFactory != null) {
                    it.store(ApolloStore(cacheFactory), writeToCacheAsynchronously = true)
                } else {
                    it
                }
            }
            .build()
    }

    private fun query(
        query: Query<*>,
        fetchPolicy: FetchPolicy,
    ) {
        runBlocking {
            val response = apolloClient.query(query)
                .fetchPolicy(fetchPolicy)
                .execute()
            try {
                response.dataAssertNoErrors
                Log.d("App", "Success")
            } catch (e: Exception) {
                Log.e("App", "Error", e)
            }
        }
    }

    // Without fragments

    fun tracksById_NetworkOnly() = query(
        query = GetTracksByIdsQuery(emptyList()),
        fetchPolicy = FetchPolicy.NetworkOnly
    )

    fun tracksById_CacheOnly() = query(
        query = GetTracksByIdsQuery(emptyList()),
        fetchPolicy = FetchPolicy.CacheOnly
    )


    // With fragments

    fun tracksByIdWithFragments_NetworkOnly() = query(
        query = GetTracksByIdsWithFragmentsQuery(emptyList()),
        fetchPolicy = FetchPolicy.NetworkOnly
    )

    fun tracksByIdWithFragments_CacheOnly() = query(
        query = GetTracksByIdsWithFragmentsQuery(emptyList()),
        fetchPolicy = FetchPolicy.CacheOnly
    )
}
