package org.example.project.dao

import entity.User
import org.example.project.entity.Users
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object AccountDao {
    fun getAccountInfo(uid: Int): Result<User> {
        return transaction {
            val user = Users.select { Users.id eq uid }
            val first = user.firstOrNull()
                ?: return@transaction Result.failure(Exception("User not found"))
            return@transaction Result.success(Users.asUser(first))
        }
    }
}