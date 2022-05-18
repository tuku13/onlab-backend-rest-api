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

    fun extractUserId(token: String) = extractAllClaim(token).get("user-id", Integer::class.java)

    private fun extractExpiration(token: String) = extractAllClaim(token).expiration

    private fun extractAllClaim(token: String) = Jwts.parser().setSigningKey(SecretKey).parseClaimsJws(token).body

    fun createToken(username: String, userId: Long): String = Jwts.builder()
        .setIssuedAt(Date(System.currentTimeMillis()))
        .setExpiration(Date(System.currentTimeMillis() + 10 * 60 * 1000L))
        .setSubject(username)
        .addClaims(mapOf("user-id" to userId))
        .signWith(SignatureAlgorithm.HS256, SecretKey)
        .compact()

    fun validateToken(token: String, username: String, userId: Long): Boolean {

        val isNotExpired = !isTokenExpired(token)
        val isIDValid = extractUserId(token).toLong() == userId
        val isUsernameValid = extractUsername(token) == username

        val isTokenValid = isNotExpired && isIDValid && isUsernameValid

        return isTokenValid
    }

    private fun isTokenExpired(token: String) = extractExpiration(token).before(Date())

}