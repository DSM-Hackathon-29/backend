package kr.hs.dsm.backend.domain.institution.persistence

import org.springframework.data.repository.CrudRepository

interface SuggestionTypesRepository : CrudRepository<SuggestionTypes, Long> {
    fun deleteByInstitutionId(institutionId: Long)
}