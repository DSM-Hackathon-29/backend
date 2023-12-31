package kr.hs.dsm.backend.domain.statistic.persistence

import org.springframework.data.repository.CrudRepository

interface SuggestionKeywordRepository : CrudRepository<SuggestionKeyword, Long> {
    fun existsBySuggestionId(suggestionId: Long): Boolean
}