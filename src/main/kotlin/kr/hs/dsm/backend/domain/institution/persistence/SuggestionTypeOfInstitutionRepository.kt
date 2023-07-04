package kr.hs.dsm.backend.domain.institution.persistence

import org.springframework.data.repository.CrudRepository

interface SuggestionTypeOfInstitutionRepository : CrudRepository<SuggestionTypeOfInstitution, Long> {
    fun deleteByInstitutionId(institutionId: Long)
}