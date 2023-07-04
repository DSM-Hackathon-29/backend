package kr.hs.dsm.backend.domain.institution.persistence

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.Table
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType

@Table
@Entity
data class Institution(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    var accountId: String,

    var password: String,

    var institutionName: String,

) {
    @OneToMany(mappedBy = "institution")
    var suggestionTypeOfInstitutions: MutableList<kr.hs.dsm.backend.domain.institution.persistence.SuggestionTypeOfInstitution> = mutableListOf()

    @OneToMany(mappedBy = "institution")
    var rangePoints: MutableList<RangePoint> = mutableListOf()

    fun isContainPoint(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Boolean {
        //crosses: 점 q와 오른쪽 반직선과 다각형과의 교점의 개수
        val p = rangePoints
        var crosses = 0
        for (i in 0 until p.size) {
            val j = (i + 1) % p.size
            // 점 B가 선분 (p[i], p[j])의 longitude 좌표 사이에 있음
            if (p[i].longitude > longitude != p[j].longitude > longitude) {
                val atLatitude =
                    (p[j].latitude - p[i].latitude) * (longitude - p[i].longitude) / (p[j].longitude - p[i].longitude) + p[i].latitude
                if (latitude < atLatitude) crosses++
            }
        }
        return crosses % 2 > 0
    }

    fun isContainType(type: SuggestionType) =
        suggestionTypeOfInstitutions.any { it.type == type }
}