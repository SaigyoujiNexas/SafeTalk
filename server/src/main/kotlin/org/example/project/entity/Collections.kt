package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Collections: IntIdTable() {
    val uid = integer("uid")
    val communityId = integer("community_id")
}