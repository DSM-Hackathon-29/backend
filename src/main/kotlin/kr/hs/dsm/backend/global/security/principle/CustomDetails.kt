package kr.hs.dsm.backend.global.security.principle

import java.util.UUID
import kr.hs.dsm.backend.domain.institution.persistence.Institution

interface CustomDetails {
    val institution: Institution
}
