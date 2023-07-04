package kr.hs.dsm.backend.global.security.principle

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import kr.hs.dsm.backend.global.security.exception.InvalidTokenException
import kr.hs.dsm.backend.domain.institution.persistence.InstitutionRepository
import org.springframework.data.repository.findByIdOrNull

@Component
class CustomDetailsService(
    private val institutionRepository: InstitutionRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        val user = username?.let { institutionRepository.findByIdOrNull(it.toLong()) }
        user.apply {
            if (this == null) {
                throw InvalidTokenException
            } else {
                return CustomDetailsImpl(
                    institution = this
                )
            }
        }
    }
}
