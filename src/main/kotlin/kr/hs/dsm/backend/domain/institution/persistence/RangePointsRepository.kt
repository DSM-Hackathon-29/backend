package kr.hs.dsm.backend.domain.institution.persistence

import org.springframework.data.repository.CrudRepository

interface RangePointsRepository : CrudRepository<RangePoints, Long> {
    fun deleteByInstitutionId(institutionId: Long)
}