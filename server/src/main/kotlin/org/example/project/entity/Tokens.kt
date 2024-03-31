package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Tokens : IntIdTable(){
    val uid  = integer("uid").entityId()
    val token = varchar("token", 3000).nullable()
}