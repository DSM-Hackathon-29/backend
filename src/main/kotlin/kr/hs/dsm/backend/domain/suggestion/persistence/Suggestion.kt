package kr.hs.dsm.backend.domain.suggestion.persistence

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.validation.constraints.Digits;
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table
import kr.hs.dsm.backend.domain.institution.persistence.SuggestionTypeOfInstitution
import kr.hs.dsm.backend.domain.statistic.persistence.SuggestionKeyword
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType

@Table
@Entity
class Suggestion(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    val title: String,

    val description: String,

    val type: SuggestionType,

    @Digits(integer = 2, fraction = 8)
    val latitude: BigDecimal,

    @Digits(integer = 2, fraction = 8)
    val longitude: BigDecimal,

    val imageUrl: String?,

    val createdAt: LocalDateTime = LocalDateTime.now()
)