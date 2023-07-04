package kr.hs.dsm.backend.global.security.principle

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import kr.hs.dsm.backend.domain.institution.persistence.Institution

class CustomDetailsImpl(
    override val institution: Institution
) : UserDetails, CustomDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun getPassword(): String? = null

    override fun getUsername(): String = institution.id.toString()

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true
}
