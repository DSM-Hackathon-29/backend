package kr.hs.dsm.backend.domain.institution.persistence

import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType

@Table
@Entity
class SuggestionTypes(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    val type: SuggestionType,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    val institution: Institution
)