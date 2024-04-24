package org.example.project.entity

import entity.account.User
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Users: IntIdTable() {
    val username = varchar("username", 50)
    val password = varchar("password", 20)
    val email = varchar("email", 50).default("").index()
    val phone = varchar("phone", 20).default("").index()
    val avatar = varchar("avatar", 100).default("https://s2.loli.net/2024/01/29/K9Pc4wosiGeWvlu.png")

    fun asUser(row: ResultRow): User =
        User(
            uid = row[id].value,
            username = row[username],
            email = row[email],
            phone = row[phone],
            avatar = row[avatar],
        )
}