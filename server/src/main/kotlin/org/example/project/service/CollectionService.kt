package org.example.project.service

import client_api.CollectionService
import entity.collection.CollectionInfo
import org.example.project.dao.CollectionDao

object CollectionService {
    fun addCollection(uid: Int, cid: Int): Result<Unit>{
        return CollectionDao.addCollection(uid, cid)
    }
    fun removeCollection(uid: Int, cid: Int): Result<Unit>{
        return CollectionDao.removeCollection(uid, cid)
    }
    fun getCollections(uid: Int, currentUserId: Int): Result<List<CollectionInfo>>{
        return CollectionDao.getCollections(uid)

    }
}