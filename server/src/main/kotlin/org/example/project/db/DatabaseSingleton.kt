package org.example.project.db

import org.example.project.entity.Tokens
import org.example.project.entity.Users
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseSingleton {
    fun init() {
        val driverClassName = "com.mysql.cj.jdbc.Driver"
        val url = "jdbc:mysql://localhost:3306/safetalk?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8&useSSL=false"
        val database = Database.connect(url, driverClassName, "yuki", "11111010010")
        transaction(database){
            SchemaUtils.create(Users)
            SchemaUtils.create(Tokens)
        }
    }
}