package kr.hs.dsm.backend.domain.suggestion.persistence

import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionStatus
import org.springframework.data.repository.CrudRepository

interface SuggestionOfInstitutionRepository : CrudRepository<SuggestionOfInstitution, SuggestionOfInstitutionId> {
    fun findBySuggestionIdAndInstitutionId(suggestionId: Long, institutionId: Long): SuggestionOfInstitution?
    fun findByInstitutionId(institutionId: Long): List<SuggestionOfInstitution>
    fun findByInstitutionIdAndStatus(institutionId: Long, status: SuggestionStatus): List<SuggestionOfInstitution>
}