package kr.hs.dsm.backend.domain.statistic.persistence

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import kr.hs.dsm.backend.domain.suggestion.persistence.Suggestion

@Table
@Entity
@IdClass(SuggestionKeywordId::class)
class SuggestionKeyword(

    @Id
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_id")
    var suggestion: Suggestion,

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    var keyword: Keyword
)

class SuggestionKeywordId : Serializable {
    private var suggestion: Long? = null
    private var keyword: Long? = null
}