package hu.tuku13.onlabrestapi.security

import hu.tuku13.onlabrestapi.repository.UserRepository
import hu.tuku13.onlabrestapi.service.JwtService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtRequestFilter : OncePerRequestFilter() {
    @Autowired
    private lateinit var jwtService: JwtService

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authorizationHeader = request.getHeader("Authorization")
            ?: return filterChain.doFilter(request, response)

        if (!authorizationHeader.startsWith("Bearer ")) {
            return
        }

        val jwt = authorizationHeader.substring(7)
        val username = jwtService.extractUsername(jwt)
        val user = userRepository.getUserByName(username).get()

        if (!jwtService.validateToken(jwt, user.name, user.id)) {
            filterChain.doFilter(request, response)
            return
        }

        val principal = user.id
        val authentication = UsernamePasswordAuthenticationToken(principal, null, emptyList())

        authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
        SecurityContextHolder.getContext().authentication = authentication

        filterChain.doFilter(request, response)
    }
}