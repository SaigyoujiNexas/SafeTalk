package org.example.project.service

import entity.BaseResponse
import entity.community.CommunityContent
import org.example.project.dao.CommunityDao

object CommunityService {
    fun addCommunityContent(content: CommunityContent): BaseResponse<Unit?> {
        val res = CommunityDao.insertCommunityContent(content)
        if (res.isSuccess) {
            return BaseResponse(code = 200, data = null, msg = "success")
        }else {
            val exception = res.exceptionOrNull()
            return BaseResponse(code = 500, data = null, msg = exception?.localizedMessage?:exception?.message?:"发布失败")
        }
    }
}