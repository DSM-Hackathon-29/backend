package kr.hs.dsm.backend.domain.statistic.persistence

import java.io.Serializable
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.IdClass
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToOne
import javax.persistence.Table
import kr.hs.dsm.backend.domain.suggestion.persistence.Suggestion

@Table
@Entity
class SuggestionKeyword(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suggestion_id")
    var suggestion: Suggestion,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keyword_id")
    var keyword: Keyword
)