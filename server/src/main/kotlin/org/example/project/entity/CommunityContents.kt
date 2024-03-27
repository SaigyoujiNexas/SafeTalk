package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object CommunityContents: IntIdTable(){
    val uid = integer("uid").entityId()
    val title = varchar("title", 100)
    val content = text("content")
    val createTime = long("create_time")
    val solved = bool("solved")
}