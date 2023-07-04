package kr.hs.dsm.backend.common.util

import kr.hs.dsm.backend.global.security.principle.CustomDetails
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {
    fun getCurrentInstitution() =
        (SecurityContextHolder.getContext().authentication.principal as CustomDetails).institution
}