package kr.hs.dsm.backend.global.filter

import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import kr.hs.dsm.backend.global.security.token.JwtParser
import kr.hs.dsm.backend.global.security.token.JwtProperties

class JwtFilter(
    private val jwtParser: JwtParser
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val token = resolvedToken(request)

        SecurityContextHolder.clearContext()
        token?.let {
            SecurityContextHolder.getContext().authentication = jwtParser.getAuthentication(token)
        }

        filterChain.doFilter(request, response)
    }

    private fun resolvedToken(request: HttpServletRequest): String? =
        request.getHeader(JwtProperties.HEADER)?.also {
            if (it.startsWith(JwtProperties.PREFIX)) {
                return it.substring(JwtProperties.PREFIX.length)
            }
        }
}
