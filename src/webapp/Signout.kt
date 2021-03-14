package com.frontado.webapp

import com.frontado.model.EPSession
import com.frontado.redirect
import com.frontado.repository.Repository
import io.ktor.application.*
import io.ktor.locations.*
import io.ktor.routing.*
import io.ktor.sessions.*

const val SIGNOUT = "/signout"

@Location(SIGNOUT)
class SignOut

fun Route.signout() {
    get<SignOut> {
        call.sessions.clear<EPSession>()
        call.redirect(SignIn())
    }
}