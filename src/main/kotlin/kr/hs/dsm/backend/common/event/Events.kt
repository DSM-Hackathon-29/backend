package kr.hs.dsm.backend.common.event

import kr.hs.dsm.backend.domain.suggestion.persistence.Suggestion
import org.springframework.context.ApplicationEvent

class SuggestionEvent(
    val suggestion: Suggestion
) : ApplicationEvent(suggestion)