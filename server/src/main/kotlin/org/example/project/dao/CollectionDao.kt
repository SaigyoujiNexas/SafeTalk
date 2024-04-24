package org.example.project.dao

import entity.account.Collection
import org.example.project.entity.Collections
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction

object CollectionDao {

    fun addCollection(uid: Int, communityId: Int): Result<Unit>{
        return transaction {
            if(AccountDao.getAccountInfo(uid).isFailure){
                return@transaction Result.failure(NoSuchElementException("no user exists"))
            }
            if(CommunityDao.getContentDetail(communityId).isFailure){
                return@transaction Result.failure(NoSuchElementException("no community content exists"))
            }
            Collections.insert {
                it[Collections.uid] = uid
                it[Collections.communityId] = communityId
            }
            return@transaction Result.success(Unit)
        }
    }
}