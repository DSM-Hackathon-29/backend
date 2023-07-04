package kr.hs.dsm.backend.domain.statistic.persistence

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Index
import javax.persistence.Table

@Table(indexes = [Index(name = "wordidx", columnList = "word")])
@Entity
class Keyword(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0L,

    val word: String
)