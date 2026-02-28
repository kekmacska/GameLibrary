package org.kekmacska.gamelibrary.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.logging.SIMPLE
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.kekmacska.gamelibrary.BuildConfig
import org.kekmacska.gamelibrary.models.Game

object KtorClientProvider {
    val client: HttpClient by lazy {
        HttpClient(OkHttp) {
            install(Logging) {
                logger = Logger.SIMPLE
                level = LogLevel.INFO
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = false
                })
            }
        }
    }
}

suspend fun getAllGames(): List<Game> {
    return KtorClientProvider.client.get(BuildConfig.API_URL).body()
}

