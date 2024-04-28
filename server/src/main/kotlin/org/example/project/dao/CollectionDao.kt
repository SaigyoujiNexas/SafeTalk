package org.example.project.dao

import entity.account.Collection
import entity.collection.CollectionInfo
import entity.community.CommunityInfo
import org.example.project.entity.Collections
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

object CollectionDao {

    fun getCollections(uid: Int): Result<List<CollectionInfo>>{
        return transaction {
            try {
                if (AccountDao.getAccountInfo(uid).isFailure) {
                    return@transaction Result.failure(NoSuchElementException("No Such User"))
                }
                val res = Collections.select { Collections.uid eq uid }.map {
                    val res = Collections.asCollections(it)
                    if (res.isSuccess)
                        res.getOrThrow()
                    else
                        throw NoSuchElementException("No Such CollectionInfo")
                }
                return@transaction Result.success(res)
            }catch (e: Throwable){
                return@transaction Result.failure(e)
            }
        }
    }
    fun addCollection(uid: Int, communityId: Int): Result<Unit>{
        return transaction {
            if(AccountDao.getAccountInfo(uid).isFailure){
                return@transaction Result.failure(NoSuchElementException("no user exists"))
            }
            if(CommunityDao.getContentDetail(communityId).isFailure){
                return@transaction Result.failure(NoSuchElementException("no community content exists"))
            }
            if(Collections.select { (Collections.uid eq uid) and (Collections.communityId eq communityId) }.empty()) {
                Collections.insert {
                    it[Collections.uid] = uid
                    it[Collections.communityId] = communityId
                }
                return@transaction Result.success(Unit)
            }else{
                return@transaction Result.failure(Exception("Already in collections"))
            }
        }
    }

    fun removeCollection(uid: Int, communityId: Int): Result<Unit>{
        return transaction {
            if(AccountDao.getAccountInfo(uid).isFailure){
                return@transaction Result.failure(NoSuchElementException("No User Exist"))
            }
            Collections.deleteWhere { (Collections.uid eq uid) and (Collections.communityId eq communityId) }
            return@transaction Result.success(Unit)
        }
    }
    fun isCollected(uid: Int, cid: Int): Boolean{
        return transaction {
            if(AccountDao.getAccountInfo(uid).isFailure){
                return@transaction false;
            }
            return@transaction Collections.select { (Collections.uid eq uid) and (Collections.communityId eq cid) }
                .limit(1)
                .toList().isNotEmpty()
        }
    }
}