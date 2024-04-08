package com.example.myapplication

import androidx.test.platform.app.InstrumentationRegistry
import com.apollographql.apollo3.calendar.operation.ItemsQuery
import okio.Buffer
import okio.BufferedSource
import okio.source
import java.io.File

object Utils {
    private val cache = mutableMapOf<Int, BufferedSource>()

    const val dbName = "testDb"
    val dbFile: File = InstrumentationRegistry.getInstrumentation().context.getDatabasePath(dbName)
    val responseBasedQuery = com.apollographql.apollo3.calendar.response.ItemsQuery(endingAfter = "", startingBefore = "")
    val operationBasedQuery = ItemsQuery(endingAfter = "", startingBefore = "")

    /**
     * Reads a resource into a fully buffered [BufferedSource]. This function returns a peeked [BufferedSource]
     * so that very little allocation is done and the segments can be reused across invocations
     *
     * @return a [BufferedSource] containing the given resource id
     */
    fun resource(id: Int): BufferedSource {
        return cache.getOrPut(id) {
            InstrumentationRegistry.getInstrumentation().targetContext.resources.openRawResource(id)
                .source()
                .use {
                    Buffer().apply {
                        writeAll(it)
                    }
                }
        }.peek()
    }


}
