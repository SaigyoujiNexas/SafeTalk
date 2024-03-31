package org.example.project.entity

import entity.community.Comment
import entity.community.CommunityContent
import org.example.project.util.toDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

object CommunityContents: IntIdTable(){
    val uid = integer("uid").entityId()
    val title = varchar("title", 100)
    val content = text("content")
    val createTime = long("create_time")
    val solved = bool("solved")

    fun asCommunityContent(row: ResultRow): Result<CommunityContent> {
        return Result.success(CommunityContent(
            id = row[id].value,
            title = row[title],
            content = row[content],
            date = row[createTime].toDate(),
            solved = row[solved],
            user = Users.asUser(Users.select { Users.id eq row[uid] }.firstOrNull()?:return Result.failure(NoSuchElementException("No author info"))),
            comments = Comments.select { Comments.communityId eq row[id].value }.map { Comments.asComment(it) }.mapNotNull { it.getOrNull() },
            images = Images.select { Images.communityId eq row[id].value }.map { it[Images.url] }
        ))
    }
}