package kr.hs.dsm.backend.common.event

import io.github.flashvayne.chatgpt.service.ChatgptService
import kr.hs.dsm.backend.domain.statistic.persistence.Keyword
import kr.hs.dsm.backend.domain.statistic.persistence.KeywordRepository
import kr.hs.dsm.backend.domain.statistic.persistence.SuggestionKeyword
import kr.hs.dsm.backend.domain.statistic.persistence.SuggestionKeywordRepository
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class EventListener(
    private val chatgptService: ChatgptService,
    private val keywordRepository: KeywordRepository,
    private val suggestionKeywordRepository: SuggestionKeywordRepository
) {


    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Async
    fun onSuggestionEvent(event: SuggestionEvent) {
        println("EventListener.onSuggestionEvent")
        println(event.suggestion)
        val suggestion = event.suggestion
        val word = chatgptService.sendMessage("'" + event.suggestion.description + "'의 핵심 단어 하나만 말해줄래?")
        println("word = $word")

        val keyword = keywordRepository.findByWord(word) ?:
            keywordRepository.save(Keyword(word = word))

        suggestionKeywordRepository.save(
            SuggestionKeyword(
                suggestion = suggestion,
                keyword = keyword
            )
        )
    }
}