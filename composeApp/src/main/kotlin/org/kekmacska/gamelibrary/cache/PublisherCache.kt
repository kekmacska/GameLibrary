package org.kekmacska.gamelibrary.cache

import android.content.Context
import kotlinx.serialization.json.Json
import org.kekmacska.gamelibrary.models.Publisher
import java.io.File

class PublisherCache(context: Context) {

    private val json = Json { ignoreUnknownKeys = true }
    private val cacheFile = File(context.filesDir, "publishers.json")

    private var inMemoryCache: MutableMap<Int, Publisher>? = null

    private fun loadCache(): MutableMap<Int, Publisher> {
        if (inMemoryCache != null) return inMemoryCache!!
        return if (cacheFile.exists()) {
            try {
                val content = cacheFile.readText()
                json.decodeFromString<MutableMap<Int, Publisher>>(content)
            } catch (_: Exception) {
                mutableMapOf()
            }
        } else {
            mutableMapOf()
        }.also { inMemoryCache = it }
    }

    fun getPublisher(publisherId: Int): Publisher? {
        return loadCache()[publisherId]
    }

    fun savePublisher(publisher: Publisher) {
        val map = loadCache()
        map[publisher.id] = publisher
        cacheFile.writeText(json.encodeToString(map))
    }
}