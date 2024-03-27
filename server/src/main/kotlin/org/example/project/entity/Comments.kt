package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Comments: IntIdTable() {
    val communityId = integer("community_id")
    val uid = integer("uid").entityId()
    val content = text("content")
    val createTime = long("create_time")
    val isBestAnswer = bool("is_best_answer")
}