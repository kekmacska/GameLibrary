package org.kekmacska.gamelibrary.services

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.KSerializer
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import org.kekmacska.gamelibrary.BuildConfig
import org.kekmacska.gamelibrary.models.Collectible
import org.kekmacska.gamelibrary.models.Game
import org.kekmacska.gamelibrary.models.LoginRequest
import org.kekmacska.gamelibrary.models.LoginResponse
import org.kekmacska.gamelibrary.models.RegisterRequest
import org.kekmacska.gamelibrary.models.RegisterResponse

object KtorClientProvider {

    val client: HttpClient by lazy {
        HttpClient(OkHttp) {

            install(Logging) {
                logger = object : Logger {
                    override fun log(message: String) {
                        println("KTOR DEBUG: $message")
                    }
                }
                level = LogLevel.ALL
            }

            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                        prettyPrint = false

                        serializersModule = SerializersModule {
                            polymorphic(RegisterResponse::class) {

                                subclass(
                                    RegisterResponse.Success::class,
                                    RegisterResponse.Success.serializer()
                                )

                                subclass(
                                    RegisterResponse.Error::class,
                                    RegisterResponse.Error.serializer()
                                )

                                defaultDeserializer { _ ->
                                    RegisterResponseSerializer
                                }
                            }
                        }
                    }
                )
            }
        }
    }
}

object RegisterResponseSerializer :
    JsonContentPolymorphicSerializer<RegisterResponse>(RegisterResponse::class) {

    override fun selectDeserializer(element: JsonElement): KSerializer<out RegisterResponse> {
        return if (element.jsonObject.containsKey("token")) {
            RegisterResponse.Success.serializer()
        } else {
            RegisterResponse.Error.serializer()
        }
    }
}

suspend fun getAllGames(): List<Game> {
    return KtorClientProvider.client.get("${BuildConfig.API_URL}/games").body()
}

suspend fun login(email: String, password: String): LoginResponse {
    return KtorClientProvider.client.post("${BuildConfig.API_URL}/login") {
        contentType(ContentType.Application.Json)
        setBody(LoginRequest(email, password))
    }.body()
}

suspend fun register(name:String,email: String,password: String): RegisterResponse{
    return KtorClientProvider.client.post("${BuildConfig.API_URL}/register"){
        contentType(ContentType.Application.Json)
        setBody(RegisterRequest(name,email,password))
    }.body()
}

suspend fun getCollectiblesForGame(gameId: Int): List<Collectible> {
    return KtorClientProvider.client.get("${BuildConfig.API_URL}/games/$gameId/collectibles").body()
}