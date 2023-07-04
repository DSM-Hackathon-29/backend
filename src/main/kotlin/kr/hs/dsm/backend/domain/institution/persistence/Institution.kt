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
    var suggestionTypes: MutableList<SuggestionTypes> = mutableListOf()

    @OneToMany(mappedBy = "institution")
    var rangePoints: MutableList<RangePoints> = mutableListOf()

    fun isContainPoint(
        latitude: BigDecimal,
        longitude: BigDecimal
    ): Boolean {
        return rangePoints
    }

    fun isPointInPolygon(x: BigDecimal, y: BigDecimal): Boolean {
        var crosses = 0;  //점과 오른쪽 반직선과 다각형과의 교점 개수

        var j = 0
        val size = rangePoints.size
        rangePoints.mapIndexed { idx, rangePoint ->
            j = idx % size + 1;    // array에 1~N가지 존재

            // tx, ty가  점 i,j 의 Y 좌료 사이에 있는 경우

            if((rangePoint.latitude > y) != (rangePoints[j].latitude > y)) {  // 둘다 크거나, 둘다 작으면 밖에 점

                val atX = (
                        rangePoints[j].longitude-rangePoint.longitude
                        ) * (y-rangePoint.latitude) / (rangePoints[j].latitude - rangePoint.latitude) + rangePoint.latitude;

                if(x < atX) crosses++
            }
        }




        return (crosses%2 > 0);
    }

    fun isContainType(type: SuggestionType) =
        suggestionTypes.any { it.type == type }
}