package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Images: IntIdTable(){
    val uid = integer("uid").nullable()
    val communityId = integer("community_id").nullable()
    val commentId = integer("comment_id").nullable()
    val url = varchar("url", 100)
}