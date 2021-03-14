package com.frontado.api

import com.frontado.API_VERSION
import com.frontado.api.requests.PhrasesApiRequest
import com.frontado.apiUser
import com.frontado.repository.Repository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val PHRASE_API_ENDPOINT = "$API_VERSION/phrases"

@Location(PHRASE_API_ENDPOINT)
class PhrasesApi

fun Route.phrasesApi(db: Repository) {
    authenticate("jwt") {
        get<PhrasesApi> {
            call.respond(db.phrases())
        }
        post<PhrasesApi> {
            val user = call.apiUser!!
            try {
                val request = call.receive<PhrasesApiRequest>()
                val phrase = db.add(userId = user.userId, emojiValue = request.emoji, phraseValue = request.phrase)
                if (phrase != null) {
                    call.respond(phrase)
                } else {
                    call.respondText("Invalid data received", status = HttpStatusCode.InternalServerError)
                }
            } catch (e: Throwable) {
                call.respondText("Invalid data received", status = HttpStatusCode.BadRequest)
            }
        }
    }
}