package kr.hs.dsm.backend.domain.suggestion.persistence

import org.springframework.data.repository.CrudRepository

interface SuggestionOfInstitutionRepository : CrudRepository<SuggestionOfInstitution, SuggestionOfInstitutionId> {
    fun findBySuggestionIdAndInstitutionId(suggestionId: Long, institutionId: Long): SuggestionOfInstitution?
}