package com.frontado.api

import com.frontado.API_VERSION
import com.frontado.model.EmojiPhrase
import com.frontado.model.Request
import com.frontado.repository.Repository
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

const val PHRASE_ENDPOINT = "$API_VERSION/phrase"

fun Route.phrase(db: Repository) {
//    authenticate( "auth") {
        post(PHRASE_ENDPOINT) {
            val request = call.receive<Request>()
            val phrase = db.add(userId = "", emojiValue = request.emoji, phraseValue = request.phrase)
            call.respond(phrase)
        }
//    }
}
