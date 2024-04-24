package org.example.project.util

import SERVER_PORT
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import entity.account.User
import java.util.Date

const val secret = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAtfJaLrzXILUg1U3N1KV8yJr92GHn5OtYZR7qWk1Mc4cy4JGjklYup7weMjBD9f3bBVoIsiUVX6xNcYIr0Ie0AQIDAQABAkEAg+FBquToDeYcAWBe1EaLVyC45HG60zwfG1S4S3IB+y4INz1FHuZppDjBh09jptQNd+kSMlG1LkAc/3znKTPJ7QIhANpyB0OfTK44lpH4ScJmCxjZV52mIrQcmnS3QzkxWQCDAiEA1Tn7qyoh+0rOO/9vJHP8U/beo51SiQMw0880a1UaiisCIQDNwY46EbhGeiLJR1cidr+JHl86rRwPDsolmeEF5AdzRQIgK3KXL3d0WSoS//K6iOkBX3KMRzaFXNnDl0U/XyeGMuUCIHaXv+n+Brz5BDnRbWS+2vkgIe9bUNlkiArpjWvX+2we"
const val issuer = "http://localhost:$SERVER_PORT/"
val audience = arrayOf("http://localhost:$SERVER_PORT/")
const val myRealm = "SafeTalk"

fun generateToken(user: User): String{
    val token =
        JWT.create()
            .withAudience(*audience)
            .withIssuer(issuer)
            .withClaim("uid", user.uid)
            .withExpiresAt(Date(Long.MAX_VALUE))
            .sign(Algorithm.HMAC256(secret))
    return token
}


