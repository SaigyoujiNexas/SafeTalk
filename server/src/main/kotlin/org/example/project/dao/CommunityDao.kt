package org.example.project.dao

import entity.community.Comment
import entity.community.CommunityContent
import org.example.project.entity.Comments
import org.example.project.entity.CommunityContents
import org.example.project.util.toDate
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object CommunityDao {
    fun getCommunityContents(keyWord: String = ""): Result<List<CommunityContent>> {
        val communityContents = mutableListOf<CommunityContent>()
        transaction {
            if (keyWord.isNotEmpty()) {
                CommunityContents.select { CommunityContents.title like "%$keyWord%" }
            } else {
                CommunityContents.selectAll()
            }.forEach {
                val user = AccountDao.getAccountInfo(it[CommunityContents.uid].value)
                if (user.isFailure) {
                    return@transaction Result.failure<Throwable>(user.exceptionOrNull()!!)
                }
                val title = it[CommunityContents.title]
                val content = it[CommunityContents.content]
                val createTime = it[CommunityContents.createTime]
                val id = it[CommunityContents.id].value
                val images = ImageDao.getCommunityContentsImage(id)
                if (images.isFailure) {
                    return@transaction Result.failure<Throwable>(images.exceptionOrNull()!!)
                }
                val communityContent = CommunityContent(
                    user = user.getOrNull()!!,
                    title = title,
                    content = content,
                    date = createTime.toDate(),
                    id = id,
                    solved = it[CommunityContents.solved],
                    images = images.getOrNull()!!,
                    comments = getComments(id)
                )
                communityContents.add(communityContent)
            }
        }
        return Result.success(communityContents)
    }

    private fun getComments(communityId: Int): List<Comment> {
        val comments = mutableListOf<Comment>()
        transaction {
            Comments.select { Comments.communityId eq communityId }.forEach {
                val user = AccountDao.getAccountInfo(it[Comments.uid].value)
                if (user.isFailure) {
                    return@transaction Result.failure<Throwable>(user.exceptionOrNull()!!)
                }
                val content = it[Comments.content]
                val createTime = it[Comments.createTime]
                val id = it[Comments.id].value
                val images = ImageDao.getCommentImage(id)
                if (images.isFailure) {
                    return@transaction Result.failure<Throwable>(images.exceptionOrNull()!!)
                }
                val comment = Comment(
                    user = user.getOrNull()!!,
                    content = content,
                    date = createTime.toDate(),
                    id = id,
                    images = images.getOrNull()!!,
                    isBestAnswer = it[Comments.isBestAnswer]
                )
                comments.add(comment)
            }
        }
        return comments
    }
    fun insertCommunityContent(content: CommunityContent): Result<Unit>{
        try {
            transaction {
                val ans = CommunityContents.insert {
                    it[uid] = content.user!!.uid
                    it[title] = content.title
                    it[CommunityContents.content] = content.content
                    it[createTime] = System.currentTimeMillis()
                    it[solved] = content.solved
                }
                content.images.forEach {
                    ImageDao.insertImage(communityContentId = ans[CommunityContents.id].value, imageUrl = it)
                }
            }
        }catch (e: Exception){
            return Result.failure(e)
        }
        return Result.success(Unit)
    }
}