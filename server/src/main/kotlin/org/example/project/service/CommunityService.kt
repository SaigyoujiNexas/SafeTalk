package org.example.project.service

import client_api.CommunityService
import client_api.UserService
import entity.BaseResponse
import entity.community.Comment
import entity.community.CommunityContent
import entity.community.NewComment
import entity.community.NewContent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.example.project.dao.CommunityDao
import org.example.project.util.toDate
import java.net.ConnectException

object CommunityService {
    fun addCommunityContent(content: NewContent, uid: Int): Result<Unit> {
        val user = AccountService.getAccountInfo(uid).getOrElse {
            return Result.failure(it)
        }
        val images = content.images.map {
            runBlocking {
                ImageService.uploadImage(it)
            }
        }
        images.any {
            it.isFailure
        }.let { isFailure ->
            if(isFailure){
                return Result.failure(ConnectException("Not image bed connection"))
            }
        }
        val realContent = CommunityContent(
            user = user,
            title = content.title,
            content = content.content,
            images = images.mapNotNull { it.getOrNull() },
            date = System.currentTimeMillis().toDate());
        val res = CommunityDao.insertCommunityContent(realContent)
        if (res.isSuccess) {
            return Result.success(Unit)
        }else {
            val exception = res.exceptionOrNull()
            return Result.failure(exception?:Exception("Unknown exception occured"))
        }
    }
    fun getContent(keyword: String): Result<List<CommunityContent>>{
        return CommunityDao.getCommunityContents(keyword)

    }
    fun getContentDetail(cid: Int):Result<CommunityContent>{
        return CommunityDao.getContentDetail(cid)
    }

    fun addComment(comment: NewComment, uid: Int): Result<Unit>{
        AccountService.getAccountInfo(uid).getOrElse {
            return Result.failure(it)
        }.let {
            return runBlocking {
                val realComment = Comment(
                    user = it,
                    communityId = comment.communityId,
                    fatherCommentId = comment.fatherCommentId,
                    content = comment.content,
                    images = comment.images.map {
                        ImageService.uploadImage(it)
                    }.takeUnless{ it.any(Result<String>::isFailure) }?.mapNotNull { it.getOrNull() }?:return@runBlocking Result.failure(ConnectException()),
                    date = System.currentTimeMillis().toDate(),
                )
                return@runBlocking CommunityDao.addComment(realComment)
            }
        }

    }
}