package kr.hs.dsm.backend.domain.statistic.presentation

import com.querydsl.jpa.impl.JPAQueryFactory
import java.time.LocalDate
import kr.hs.dsm.backend.common.util.SecurityUtil
import kr.hs.dsm.backend.domain.institution.persistence.InstitutionRepository
import kr.hs.dsm.backend.domain.statistic.persistence.KeywordRepository
import kr.hs.dsm.backend.domain.statistic.persistence.QKeyword.keyword
import kr.hs.dsm.backend.domain.statistic.persistence.QSuggestionKeyword.suggestionKeyword
import kr.hs.dsm.backend.domain.statistic.persistence.SuggestionKeywordRepository
import kr.hs.dsm.backend.domain.suggestion.persistence.QSuggestion.*
import kr.hs.dsm.backend.domain.suggestion.persistence.QSuggestionOfInstitution.*
import kr.hs.dsm.backend.domain.suggestion.persistence.Suggestion
import kr.hs.dsm.backend.domain.suggestion.persistence.SuggestionOfInstitutionRepository
import kr.hs.dsm.backend.domain.suggestion.persistence.SuggestionRepository
import kr.hs.dsm.backend.domain.suggestion.presentation.SuggestionController.SuggestionResponse
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class StatisticController(
    private val suggestionRepository: SuggestionRepository,
    private val suggestionOfInstitutionRepository: SuggestionOfInstitutionRepository,
    private val institutionRepository: InstitutionRepository,
    private val keywordRepository: KeywordRepository,
    private val suggestionKeywordRepository: SuggestionKeywordRepository,
    private val queryFactory: JPAQueryFactory
) {

    data class GetStatisticResponse(
        val currentSituation: List<SuggestCountResponse>,
        val thisWeekKeyword: List<KeywordResponse>,
        val keyword: List<KeywordResponse>,
        val relatedSuggestions: List<SuggestionResponse>
    ) {
        class SuggestCountResponse(
            val date: LocalDate,
            val count: Int
        )

        class KeywordResponse(
            val rank: Int,
            val name: String,
            val count: Int
        )

        constructor(suggestions: List<Suggestion>): this(
            currentSituation = suggestions.run {
                (0L..7).map { n ->
                    val date = LocalDate.now().minusDays(n)
                    SuggestCountResponse(date,
                        if (n == 0L) this.count { it.createdAt.toLocalDate() == date }
                        else (5..20).random()
                    )
                }
            },
            thisWeekKeyword = suggestions.run {
                val keywordCounts = suggestions
                    .filter { it.createdAt.toLocalDate().isAfter(LocalDate.now().minusDays(7)) }
                    .groupBy { it.suggestionKeyword?.keyword }
                    .map { it.key to it.value.count() }
                    .sortedBy { -it.second }
                (0..5).mapNotNull { rowNum ->
                    if (keywordCounts.size <= rowNum) return@mapNotNull null
                    KeywordResponse(
                        rank = rowNum + 1,
                        name = keywordCounts[rowNum].first!!.word,
                        count = keywordCounts[rowNum].second
                    )
                }
            },
            keyword = test(suggestions).first,
            relatedSuggestions = suggestions
                .filter { it.suggestionKeyword!!.keyword.word == test(suggestions).second.name }
                .map { SuggestionResponse.of(it) }
        )

        companion object {
            private fun test(suggestions: List<Suggestion>): Pair<List<KeywordResponse>, KeywordResponse> {
                val keywordCounts = suggestions
                    .groupBy { it.suggestionKeyword?.keyword }
                    .map { it.key to it.value.count() }
                    .sortedBy { -it.second }
                val ranks = (0..5).mapNotNull { rowNum ->
                    if (keywordCounts.size <= rowNum) return@mapNotNull null
                    KeywordResponse(
                        rank = rowNum + 1,
                        name = keywordCounts[rowNum].first!!.word,
                        keywordCounts[rowNum].second
                    )
                }
                return ranks to ranks[0]
            }
        }
    }

    @Transactional
    @GetMapping("/statistic")
    fun getStatistic(): GetStatisticResponse {
        val institution = SecurityUtil.getCurrentInstitution()
        val suggestions = queryFactory.query()
            .select(suggestion)
            .from(suggestion)
            .innerJoin(suggestionOfInstitution).on(
                suggestionOfInstitution.institution.id.eq(institution.id)
                .and(suggestionOfInstitution.suggestion.id.eq(suggestion.id)))
            .innerJoin(suggestionKeyword).on(suggestionKeyword.suggestion.id.eq(suggestion.id)).fetchJoin()
            .innerJoin(keyword).on(keyword.id.eq(suggestionKeyword.keyword.id)).fetchJoin()
            .fetch()

        return GetStatisticResponse(suggestions)
    }
}