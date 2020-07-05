package com.fideliaz


import CampaignServiceImpl
import PartnerServiceImpl
import UserServiceImpl
import Usuario
import io.ktor.application.call
import io.ktor.http.HttpStatusCode
import io.ktor.request.receive
import io.ktor.response.respond
import io.ktor.routing.*
import org.slf4j.LoggerFactory

fun Route.fidelidade() {
    val userService = UserServiceImpl()
    val campaignService = CampaignServiceImpl()
    val partnerService = PartnerServiceImpl()

    val logger = LoggerFactory.getLogger("Rotas")

    route("/users") {
        get("/{id}") {
            val id = call.parameters["id"]

            val idInt = id?.toInt()!!

            val user = userService.getUser(idInt)

            call.respond(HttpStatusCode.OK, user)
        }

        post("/{id}") {
            val id = call.parameters["id"]

            val idInt = id?.toInt()!!

            val user = call.receive<Usuario>()

            user.id = idInt

            val createdUser = userService.createUser(user)

            call.respond(HttpStatusCode.Created, createdUser)
        }

        delete("/{id}") {
            val id = call.parameters["id"]

            val idInt = id?.toInt()!!

            val user = userService.getUser(idInt)
            userService.deleteUser(user)

            call.respond(HttpStatusCode.OK, mapOf("$id" to "deleted"))
        }
    }

}