package kr.hs.dsm.backend.domain.institution.persistence

import java.math.BigDecimal
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType

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
        return true
    }

    fun isContainType(type: SuggestionType) =
        suggestionTypes.any { it.type == type }
}