package org.example.project.entity

import entity.community.Comment
import entity.community.CommunityDetail
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
    val deleted = bool("deleted").default(false)
    val deleteReason = varchar("delete_reason", 30).default("")

    fun asCommunityDetail(row: ResultRow): Result<CommunityDetail> {
        return Result.success(CommunityDetail(
            id = row[id].value,
            deleted = row[deleted],
            deleteReason = row[deleteReason],
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