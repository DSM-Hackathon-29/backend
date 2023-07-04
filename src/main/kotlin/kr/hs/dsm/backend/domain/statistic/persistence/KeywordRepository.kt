package kr.hs.dsm.backend.domain.statistic.persistence

import org.springframework.data.repository.CrudRepository

interface KeywordRepository : CrudRepository<Keyword, Long> {
    fun findByWord(word: String): Keyword?
}