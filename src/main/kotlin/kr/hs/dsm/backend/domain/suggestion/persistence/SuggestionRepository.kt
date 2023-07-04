package kr.hs.dsm.backend.domain.suggestion.persistence

import org.springframework.data.repository.CrudRepository

interface SuggestionRepository : CrudRepository<Suggestion, Long>