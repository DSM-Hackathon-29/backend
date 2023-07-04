package kr.hs.dsm.backend.domain.institution.persistence

import org.springframework.data.repository.CrudRepository


interface InstitutionRepository : CrudRepository<Institution, Long> {
    fun findByAccountId(accountId: String): Institution?
}