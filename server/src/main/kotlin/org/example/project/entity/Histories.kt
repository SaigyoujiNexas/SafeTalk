package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable

object Histories: IntIdTable() {
    val uid = integer("uid")
    val communityId = integer("community_id")
    val date = long("date")
}