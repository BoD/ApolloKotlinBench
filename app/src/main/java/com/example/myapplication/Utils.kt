package com.example.myapplication

import android.content.Context
import okio.Buffer
import okio.BufferedSource
import okio.source

object Utils {
    private val cache = mutableMapOf<Int, BufferedSource>()

    /**
     * Reads a resource into a fully buffered [BufferedSource]. This function returns a peeked [BufferedSource]
     * so that very little allocation is done and the segments can be reused across invocations
     *
     * @return a [BufferedSource] containing the given resource id
     */
    fun resource(context: Context, id: Int): BufferedSource {
        return cache.getOrPut(id) {
            context.resources.openRawResource(id)
                .source()
                .use {
                    Buffer().apply {
                        writeAll(it)
                    }
                }
        }.peek()
    }
}
