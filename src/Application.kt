package com.fideliaz

import com.fasterxml.jackson.databind.SerializationFeature
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.apache.Apache
import io.ktor.features.ContentNegotiation
import io.ktor.features.StatusPages
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.jackson.jackson
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.route
import io.ktor.routing.routing
import io.ktor.server.engine.applicationEngineEnvironment
import io.ktor.server.engine.connector
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

//fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)
fun main(args: Array<String>): Unit {
    val env = applicationEngineEnvironment {
        module {
            module(false)
        }
        // Public API
        connector {
            host = "0.0.0.0"
            port = 8080
        }
    }

    embeddedServer(Netty, configure = {
        callGroupSize  = 1 //TODO: only for testing
        // Size of the queue to store [ApplicationCall] instances that cannot be immediately processed
        requestQueueLimit = 16
        // Do not create separate call event group and reuse worker group for processing calls
        shareWorkGroup = false
        // User-provided function to configure Netty's [ServerBootstrap]
        configureBootstrap = {
            // ...
        }
        // Timeout in seconds for sending responses to client
        responseWriteTimeoutSeconds = 10
    }, environment = env).start(true)

}

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    val client = HttpClient(Apache) {
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        install(StatusPages) {

            exception<IllegalArgumentException> { cause ->
                call.respond(HttpStatusCode.BadRequest, mapOf("message" to cause.message!!))
            }

        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }

        route("/api/v1") {
            fidelidade()
        }
    }

}
