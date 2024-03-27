package org.example.project.dao

import org.example.project.entity.Images
import org.example.project.entity.Users
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object ImageDao {
    fun getUserAvatar(uid: Int): Result<String> {
        transaction {
            val user = Users.select { Users.id eq uid }.firstOrNull()?:
            return@transaction Result.failure(Exception("user not found"))
            return@transaction Result.success(user[Users.avatar])
        }
        return Result.failure(Exception("user not found"))
    }
    fun getCommunityContentsImage(communityContentId: Int): Result<List<String>> {
        val returnValue = mutableListOf<String>()
        transaction {
            Images.select { Images.communityId eq communityContentId }.forEach {
                returnValue.add(it[Images.url])
            }
        }
        return Result.success(returnValue)
    }
    fun getCommentImage(commentId: Int): Result<List<String>> {
        val returnValue = mutableListOf<String>()
        transaction {
            Images.select { Images.commentId eq commentId }.forEach {
                returnValue.add(it[Images.url])
            }
        }
        return Result.success(returnValue)
    }
    fun insertImage(imageUrl: String,
                    communityContentId: Int? = null,
                    commentId: Int? = null,
                    uid: Int? = null){
        transaction {
            Images.insert {
                it[url] = imageUrl
                it[this.communityId] = communityContentId
                it[this.commentId] = commentId
                it[this.uid] = uid
            }
        }
    }
}