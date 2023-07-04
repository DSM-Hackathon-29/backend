package kr.hs.dsm.backend.domain.institution.persistence

import org.springframework.data.repository.CrudRepository

interface RangePointsRepository : CrudRepository<RangePoint, Long> {
    fun deleteByInstitutionId(institutionId: Long)
}