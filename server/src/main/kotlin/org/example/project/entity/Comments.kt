package org.example.project.entity

import entity.community.Comment
import org.example.project.util.toDate
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select

object Comments: IntIdTable() {
    val communityId = integer("community_id")
    val uid = integer("uid").entityId()
    val fatherCommentId = integer("father_comment_id").default(-1)
    val content = text("content")
    val createTime = long("create_time").default(System.currentTimeMillis())
    val isBestAnswer = bool("is_best_answer").default(false)
    fun asComment(row: ResultRow): Result<Comment> {
        return Comment(
            id = row[id].value,
            user = Users.asUser(Users.select { Users.id eq row[uid].value }.firstOrNull()?:return Result.failure(NoSuchElementException("No comment user get"))),
            content = row[content],
            date = row[createTime].toDate(),
            images = Images.select { Images.commentId eq row[id].value }.map { it[Images.url] },
            isBestAnswer = row[isBestAnswer],
            fatherCommentId = row[fatherCommentId],
            communityId = row[communityId],
            comments = Comments.select { fatherCommentId eq row[id].value }.map { asComment(it) }
                .takeUnless { it ->
                    it.any { it.isFailure }
                }?.let { it.mapNotNull { it.getOrNull() } }?: emptyList()
        ).let {
            Result.success(it)
        }
    }
}