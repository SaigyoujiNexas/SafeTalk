package org.example.project.entity

import entity.community.Comment
import entity.community.CommunityDetail
import entity.community.CommunityInfo
import org.example.project.dao.AccountDao
import org.example.project.dao.CollectionDao
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

    fun asCommunityInfo(row: ResultRow): Result<CommunityInfo> {
        val user = AccountDao.getAccountInfo(row[CommunityContents.uid].value)
        if (user.isFailure) {
            return Result.failure(user.exceptionOrNull()!!)
        }
        val title = row[CommunityContents.title]
        val createTime = row[CommunityContents.createTime]
        val id = row[CommunityContents.id].value
        return Result.success(CommunityInfo(
            userAvatar = user.getOrNull()!!.avatar,
            userName = user.getOrNull()!!.username,
            title = title,
            time = createTime.toDate(),
            id = id,
            solved = row[CommunityContents.solved],
            isCollected = CollectionDao.isCollected(user.getOrNull()!!.uid, id),
        ))
    }
}