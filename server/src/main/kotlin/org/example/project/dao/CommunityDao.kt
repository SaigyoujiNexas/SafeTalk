package org.example.project.dao

import entity.community.Comment
import entity.community.CommunityDetail
import entity.community.CommunityInfo
import org.example.project.entity.Comments
import org.example.project.entity.CommunityContents
import org.example.project.entity.Images
import org.example.project.util.toDate
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object CommunityDao {
    fun getCommunityInfos(keyWord: String = ""): Result<List<CommunityInfo>> {
        return transaction {
            if (keyWord.isNotEmpty()) {
                CommunityContents.select { CommunityContents.title like "%$keyWord%" and CommunityContents.deleted eq booleanLiteral(false) }
            } else {
                CommunityContents.select{CommunityContents.deleted eq booleanLiteral(false) }
            }.map {
                val user = AccountDao.getAccountInfo(it[CommunityContents.uid].value)
                if (user.isFailure) {
                    return@transaction Result.failure(user.exceptionOrNull()!!)
                }
                val title = it[CommunityContents.title]
                val createTime = it[CommunityContents.createTime]
                val id = it[CommunityContents.id].value
                CommunityInfo(
                    userAvatar = user.getOrNull()!!.avatar,
                    userName = user.getOrNull()!!.username,
                    title = title,
                    time = createTime.toDate(),
                    id = id,
                    solved = it[CommunityContents.solved],
                )
            }.let {

                return@transaction Result.success(it)
            }
        }
    }

    private fun getComments(communityId: Int): List<Comment> {
        return transaction {
            Comments.select { Comments.communityId eq communityId }.mapNotNull {
                Comments.asComment(it).getOrNull()
            }
        }
    }
    fun insertCommunityContent(content: CommunityDetail): Result<Unit>{
            return transaction {
                try{
                println("insert content");
                val ans = CommunityContents.insert {
                    it[uid] = content.user.uid
                    it[title] = content.title
                    it[CommunityContents.content] = content.content
                    it[createTime] = System.currentTimeMillis()
                    it[solved] = content.solved
                }
                content.images.forEach {
                    ImageDao.insertImage(communityContentId = ans[CommunityContents.id].value, imageUrl = it)
                }
            }catch (e: Exception){
                    return@transaction Result.failure<Unit>(e)
            }
                return@transaction Result.success(Unit)
        }
    }

    fun getContentDetail(cid: Int): Result<CommunityDetail>{
        return transaction {
            try{
                val res = CommunityContents.select { CommunityContents.id eq cid }.firstOrNull()?:return@transaction Result.failure(NoSuchElementException("No such community detail"))
                return@transaction CommunityContents.asCommunityDetail(res)
            }catch (e: Throwable){
                return@transaction Result.failure(e)
            }
        }
    }

    fun addComment(comment: Comment): Result<Unit>{
        return transaction {
            try{
                val insertedCommentId = Comments.insertAndGetId {
                    it[communityId] = comment.communityId
                    it[fatherCommentId] = comment.fatherCommentId
                    it[content] = comment.content
                    it[createTime] = System.currentTimeMillis()
                    it[uid] = comment.user.uid
                    it[isBestAnswer] = false
                }.value
                comment.images.forEach {imageUrl ->
                    Images.insert {
                        it[url] = imageUrl
                        it[commentId] = insertedCommentId
                        it[uid] = comment.user.uid
                    }
                }
                return@transaction Result.success(Unit)
            }catch (e: Throwable){
                return@transaction Result.failure(e)
            }
        }
    }
}