package org.example.project.entity

import entity.collection.CollectionInfo
import org.example.project.dao.CommunityDao
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object Collections: IntIdTable() {
    val uid = integer("uid")
    val communityId = integer("community_id")
    val date = long("date").default(System.currentTimeMillis())

    fun asCollections(row: ResultRow): Result<CollectionInfo> {
        val cid = row[communityId];
        val date = row[Collections.date]
        CommunityDao.getContentInfo(cid)
            .onSuccess {
                return Result.success(CollectionInfo(
                    id = row[Collections.id].value,
                    contentInfo = it,
                    date = date
                ))
            }.onFailure {
                return Result.failure(it)
            }
        return Result.failure(Exception("Unknown Exception"))
    }
}