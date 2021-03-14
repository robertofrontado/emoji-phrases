package com.frontado.repository

import com.frontado.model.EmojiPhrase
import com.frontado.model.EmojiPhrases
import com.frontado.model.User
import com.frontado.model.Users
import com.frontado.repository.DatabaseFactory.dbQuery
import org.jetbrains.exposed.sql.*
import java.lang.IllegalArgumentException

class EmojiPhrasesRepository: Repository {

    override suspend fun add(userId: String, emojiValue: String, phraseValue: String): Unit = dbQuery {
        EmojiPhrases.insert {
            it[this.userId] = userId
            it[emoji] = emojiValue
            it[phrase] = phraseValue
        }
    }

    override suspend fun phrase(id: Int): EmojiPhrase? = dbQuery {
        EmojiPhrases.select { EmojiPhrases.id eq id }
            .mapNotNull(::toEmojiPhrase)
            .singleOrNull()
    }

    override suspend fun phrase(id: String): EmojiPhrase? = phrase(id.toInt())

    override suspend fun phrases(): List<EmojiPhrase> = dbQuery {
        EmojiPhrases.selectAll().map(::toEmojiPhrase)
    }

    override suspend fun remove(id: Int): Boolean {
        if (phrase(id) == null) {
            throw IllegalArgumentException("No phrase found for id $id.")
        }
        return dbQuery {
            EmojiPhrases.deleteWhere { EmojiPhrases.id eq id } > 0
        }
    }

    override suspend fun remove(id: String): Boolean = remove(id.toInt())

    override suspend fun clear() {
        EmojiPhrases.deleteAll()
    }

    override suspend fun user(userId: String, hash: String?): User? {
        val user = dbQuery {
            Users.select { Users.id eq userId }
                .mapNotNull(::toUser)
                .singleOrNull()
        }

        return when {
            user == null -> null
            hash == null -> user
            user.passwordHash == hash -> user
            else -> null
        }
    }

    override suspend fun userByEmail(email: String): User? =
        Users.select { Users.email eq email }
            .mapNotNull(::toUser)
            .singleOrNull()

    override suspend fun createUser(user: User): Unit = dbQuery {
        Users.insert {
            it[id] = user.userId
            it[displayName] = user.displayName
            it[email] = user.email
            it[passwordHash] = user.passwordHash
        }
    }

    private fun toEmojiPhrase(row: ResultRow): EmojiPhrase =
        EmojiPhrase(
            id = row[EmojiPhrases.id].value,
            userId = row[EmojiPhrases.userId],
            emoji = row[EmojiPhrases.emoji],
            phrase = row[EmojiPhrases.phrase]
        )

    private fun toUser(row: ResultRow): User =
        User(
            userId = row[Users.id],
            email = row[Users.email],
            displayName = row[Users.displayName],
            passwordHash = row[Users.passwordHash],
        )
}