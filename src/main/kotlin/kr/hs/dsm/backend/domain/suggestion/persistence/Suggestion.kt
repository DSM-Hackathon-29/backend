package kr.hs.dsm.backend.domain.suggestion.persistence

import java.math.BigDecimal
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table
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

    val latitude: BigDecimal,

    val longitude: BigDecimal,

    val imageUrl: String?,

    val createdAt: LocalDateTime = LocalDateTime.now()
)