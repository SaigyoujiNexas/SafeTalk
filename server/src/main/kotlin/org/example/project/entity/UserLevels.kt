package org.example.project.entity

import org.jetbrains.exposed.dao.id.IntIdTable


object UserLevels : IntIdTable() {
    val level = integer("level")
    val uid = integer("uid")
}
object Levels{
    const val LEVEL_NORMAL = 0
    const val LEVEL_ADMIN = 1
    const val LEVEL_SUPER_ADMIN = 2
}
