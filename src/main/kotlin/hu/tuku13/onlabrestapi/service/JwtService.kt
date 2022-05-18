package hu.tuku13.onlabrestapi.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.stereotype.Service
import java.util.*

@Service
class JwtService {
    private val SecretKey = "onlab-secret-key-12345"

    fun extractUsername(token: String): String = extractAllClaim(token).subject

    fun extractUserId(token: String) = extractAllClaim(token)["user-id"] as? Long

    private fun extractExpiration(token: String) = extractAllClaim(token).expiration

    private fun extractAllClaim(token: String) = Jwts.parser().setSigningKey(SecretKey).parseClaimsJws(token).body

    fun createToken(username: String, userId: Long): String = Jwts.builder()
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + 10 * 60 * 1000L))
        .setSubject(username)
        .addClaims(mapOf("user-id" to userId))
        .signWith(SignatureAlgorithm.HS256, SecretKey)
        .compact()

    fun validateToken(token: String, username: String, userId: Long) = !isTokenExpired(token)
            && extractUserId(token) == userId
            && extractUsername(token) == username

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

}