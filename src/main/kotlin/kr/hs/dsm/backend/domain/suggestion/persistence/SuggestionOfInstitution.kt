package kr.hs.dsm.backend.domain.suggestion.persistence

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import kr.hs.dsm.backend.domain.institution.persistence.Institution
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionStatus


@Entity
@IdClass(SuggestionOfInstitutionId::class)
class SuggestionOfInstitution(

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_id")
    var suggestion: Suggestion?,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "institution_id")
    var institution: Institution?,

    @Enumerated(EnumType.STRING)
    var status: SuggestionStatus = SuggestionStatus.CREATED
)

class SuggestionOfInstitutionId : Serializable {
    private val suggestion: Long? = null
    private val institution: Long? = null
}