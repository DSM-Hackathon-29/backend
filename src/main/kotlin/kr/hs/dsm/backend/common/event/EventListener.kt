package kr.hs.dsm.backend.common.event

import io.github.flashvayne.chatgpt.service.ChatgptService
import kr.hs.dsm.backend.domain.statistic.persistence.Keyword
import kr.hs.dsm.backend.domain.statistic.persistence.KeywordRepository
import kr.hs.dsm.backend.domain.statistic.persistence.SuggestionKeyword
import kr.hs.dsm.backend.domain.statistic.persistence.SuggestionKeywordRepository
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.ApplicationListener
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class EventTestListener(
    private val chatgptService: ChatgptService,
    private val keywordRepository: KeywordRepository,
    private val suggestionKeywordRepository: SuggestionKeywordRepository
) : ApplicationListener<SuggestionEvent> {

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    override fun onApplicationEvent(event: SuggestionEvent) {
        val suggestion = event.suggestion
        val request = "'${suggestion.description}'라는 건의를 듣고 개선해야하는 물리적인 요소를 하나의 키워드로 요약해줘."
        val response = chatgptService.sendMessage(request)

        println("$request -> ${response.trim()}")

        val word = response.trim().split(" ")[0].trim()
        val keyword = keywordRepository.findByWord(word) ?: keywordRepository.save(Keyword(word = word))

        suggestionKeywordRepository.save(
            SuggestionKeyword(
                suggestion = suggestion,
                keyword = keyword
            )
        )
    }
}