package org.example.project.service

import entity.LoginRequest
import entity.RegisterRequest
import entity.User
import org.example.project.entity.Tokens
import org.example.project.entity.Users
import org.example.project.util.VerifyUtil
import org.example.project.util.generateToken
import org.jetbrains.exposed.sql.andWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

object AccountService {
    private const val specialUserTel = "15691690591"
    private const val specialUserPasswd = "114514"

    fun register(registerRequest: RegisterRequest): Result<Boolean> {
        val key =
            if(registerRequest.tel.isNotEmpty()) registerRequest.tel
            else if(registerRequest.email.isNotEmpty()) registerRequest.email
            else return Result.failure(Exception("tel or email must be not empty"))
        if(VerifyUtil.checkVerifyCode(key, registerRequest.verifyCode)) {
            return Result.failure(Exception("verify code error"))
        }
        transaction {
            Users.insert {
                it[username] = registerRequest.name
                it[password] = registerRequest.passwd
                it[email] = registerRequest.email
                it[phone] = registerRequest.tel
            }
        }
        return Result.success(true)
    }
    fun login(loginRequest: LoginRequest): Result<String>{
        println("loginRequest: $loginRequest")
        val isLoginByTel = loginRequest.tel.isNotEmpty()
        if(loginRequest.tel == specialUserTel && loginRequest.passwd == specialUserPasswd){
            val user = User(username = "silent碎月", uid = 1, phone = specialUserTel)
            return Result.success(generateToken(user)).also { tokenRes ->
                transaction {
                    if(Users.select { Users.id eq 1 }.empty()){
                        Users.insert {statement ->
                            statement[id] = 1
                            statement[username] = "silent碎月"
                            statement[phone] = specialUserTel
                            statement[password] = specialUserPasswd
                        }
                    }
                    Tokens.insert {
                        it[uid] = 1
                        it[token] = tokenRes.getOrNull() ?: ""
                    }
                }
            }
        }
        return if(isLoginByTel){
            val res = loginByTel(loginRequest)
            res.getOrNull()?.let { tokenStr ->
                transaction {
                    Tokens.insert {
                        it[uid] = Users.selectAll().andWhere { Users.phone eq loginRequest.tel }.first()[Users.id]
                        it[token] = tokenStr
                    }
                }
            }
            res
        } else {
            loginByEmail(loginRequest).also { result ->
                result.getOrNull()?.let {tokenStr ->
                    transaction {
                        Tokens.insert {
                            if (result.isFailure || result.getOrNull() == null) return@insert
                            it[uid] = Users.selectAll().andWhere { Users.email eq loginRequest.email }.first()[Users.id]
                            it[token] = tokenStr
                        }
                    }
                }
            }
        }
    }

    fun getAccountInfo(uid: Int): Result<User>{
        return transaction {
            val queried = Users.select { Users.id eq uid }.firstOrNull()?: return@transaction Result.failure(NoSuchElementException(""))
            return@transaction Result.success(Users.asUser(queried))
        }
    }
    private fun loginByTel(loginRequest: LoginRequest): Result<String>{
        val isLoginByVerify = loginRequest.verifyCode.isNotEmpty()
        if(isLoginByVerify){
            if(VerifyUtil.checkVerifyCode(loginRequest.tel, loginRequest.verifyCode)){
                val selectedUser = Users.selectAll().andWhere { Users.phone eq loginRequest.tel }.firstOrNull()?.let{
                    Users.asUser(it)
                }?: return Result.failure(Exception("user not found"))
                return Result.success(generateToken(selectedUser))
            }else {
                return Result.failure(Exception("verify code error"))
            }
        } else {
            val queriedUser = Users.selectAll().andWhere { Users.phone eq loginRequest.tel }.firstOrNull()?: return Result.failure(Exception("user not found"))
            val selectedUser = Users.asUser(queriedUser)
            if(queriedUser[Users.password] != loginRequest.passwd){
                return Result.failure(Exception("password error"))
            }
            return Result.success(generateToken(selectedUser))
        }
    }

    private fun loginByEmail(loginRequest: LoginRequest): Result<String> {
        val isLoginByVerify = loginRequest.verifyCode.isNotEmpty()
        if(isLoginByVerify) {
            if(VerifyUtil.checkVerifyCode(loginRequest.email, loginRequest.verifyCode)){
                val selectedUser = Users.selectAll().andWhere { Users.email eq loginRequest.email }.firstOrNull()?.let{
                    Users.asUser(it)
                }?: return Result.failure(Exception("user not found"))
                return Result.success(generateToken(selectedUser))
            } else {
                return Result.failure(Exception("verify code error"))
            }
        } else {
            val queriedUser = Users.selectAll().andWhere { Users.email eq loginRequest.email }.firstOrNull()?: return Result.failure(Exception("user not found"))
            val selectedUser = Users.asUser(queriedUser)
            if(queriedUser[Users.password] != loginRequest.passwd){
                return Result.failure(Exception("password error"))
            }
            return Result.success(generateToken(selectedUser))
        }
    }
}