package com.frontado

import io.ktor.util.*
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

const val MIN_USER_ID_LENGTH = 4
const val MIN_PASSWORD_LENGTH = 6

val hashKey = hex(System.getenv("SECRET_KEY"))
val algorithm = "HmacSHA1"
val hmacKey = SecretKeySpec(hashKey, algorithm)

fun hash(password: String): String {
    val hmac = Mac.getInstance(algorithm)
    hmac.init(hmacKey)
    return hex(hmac.doFinal(password.toByteArray(Charsets.UTF_8)))
}

private val userIdPattern = "[a-zA-Z0-9_\\.]+".toRegex()

internal fun userNameValid(userId: String): Boolean = userId.matches(userIdPattern)