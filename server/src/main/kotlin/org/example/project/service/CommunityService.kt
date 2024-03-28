package org.example.project.service

import entity.BaseResponse
import entity.community.CommunityContent
import entity.community.NewContent
import org.example.project.dao.CommunityDao
import org.example.project.util.toDate

object CommunityService {
    fun addCommunityContent(content: NewContent, uid: Int): Result<Unit> {
        val user = AccountService.getAccountInfo(uid).getOrElse {
            return Result.failure(it)
        }
        val realContent = CommunityContent(
            user = user,
            title = content.title,
            content = content.content,
            date = System.currentTimeMillis().toDate());
        val res = CommunityDao.insertCommunityContent(realContent)
        if (res.isSuccess) {
            return Result.success(Unit)
        }else {
            val exception = res.exceptionOrNull()
            return Result.failure(exception?:Exception("Unknown exception occured"))
        }
    }
}