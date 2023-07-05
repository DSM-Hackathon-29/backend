package kr.hs.dsm.backend.domain.suggestion.presentation

import com.querydsl.jpa.impl.JPAQueryFactory
import java.math.BigDecimal
import java.time.LocalDateTime
import kr.hs.dsm.backend.common.event.SuggestionEvent
import kr.hs.dsm.backend.common.util.SecurityUtil
import kr.hs.dsm.backend.domain.institution.persistence.InstitutionRepository
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionStatus
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType
import kr.hs.dsm.backend.domain.suggestion.persistence.Suggestion
import kr.hs.dsm.backend.domain.suggestion.persistence.SuggestionOfInstitution
import kr.hs.dsm.backend.domain.suggestion.persistence.SuggestionOfInstitutionRepository
import kr.hs.dsm.backend.domain.suggestion.persistence.SuggestionRepository
import kr.hs.dsm.backend.global.error.InvalidStatusException
import kr.hs.dsm.backend.global.error.NotFoundException
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.HttpStatus
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
class SuggestionController(
    private val suggestionRepository: SuggestionRepository,
    private val suggestionOfInstitutionRepository: SuggestionOfInstitutionRepository,
    private val institutionRepository: InstitutionRepository,
    private val eventPublisher: ApplicationEventPublisher
) {

    @Transactional
    @GetMapping("/suggestion")
    fun getSuggestions(): SuggestionListResponse {
        val institution = SecurityUtil.getCurrentInstitution()
        val suggestionOfInstitutions = suggestionOfInstitutionRepository.findByInstitutionId(institution.id)
        val suggestions = suggestionRepository.findAllById(suggestionOfInstitutions.map { it.suggestion!!.id })
        return SuggestionListResponse(
            suggestions.map { SuggestionResponse.of(it) }
        )
    }

    data class SuggestionListResponse(
        val suggestions: List<SuggestionResponse>
    )

    data class SuggestionResponse(
        val id: Long,
        val title: String,
        val createdAt: LocalDateTime,
        val imageUrl: String?,
        val type: SuggestionType
    ) {
        companion object {
            fun of(suggestion: Suggestion) = suggestion.run {
                SuggestionResponse(
                    id = id,
                    title = title,
                    createdAt = createdAt,
                    imageUrl = imageUrl,
                    type = type
                )
            }
        }
    }

    @Transactional
    @GetMapping("/suggestion/{suggestion-id}")
    fun getSuggestionDetail(@PathVariable("suggestion-id") suggestionId: Long): SuggestionDetailResponse {
        val suggestion = suggestionRepository.findByIdOrNull(suggestionId) ?: throw NotFoundException
        return SuggestionDetailResponse.of(suggestion)
    }

    data class SuggestionDetailResponse(
        val id: Long,
        val title: String,
        val createdAt: LocalDateTime,
        val imageUrl: String?,
        val type: SuggestionType,
        val latitude: BigDecimal,
        val longitude: BigDecimal,
        val description: String
    ) {
        companion object {
            fun of(suggestion: Suggestion) = suggestion.run {
                SuggestionDetailResponse(
                    id = id,
                    title = title,
                    createdAt = createdAt,
                    imageUrl = imageUrl,
                    type = type,
                    latitude = latitude,
                    longitude = longitude,
                    description = description
                )
            }
        }
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/suggestion/register/{suggestion-id}")
    fun register(@PathVariable("suggestion-id") suggestionId: Long) {
        val suggestion = suggestionRepository.findByIdOrNull(suggestionId) ?: throw NotFoundException
        val institution = SecurityUtil.getCurrentInstitution()

        val suggestionOfInstitution =
            suggestionOfInstitutionRepository.findBySuggestionIdAndInstitutionId(suggestion.id, institution.id)
                ?: throw NotFoundException

        if (suggestionOfInstitution.status != SuggestionStatus.CREATED) {
            throw InvalidStatusException
        }
        suggestionOfInstitution.status = SuggestionStatus.REGISTERD
        suggestionOfInstitutionRepository.save(suggestionOfInstitution)
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/suggestion/solve/{suggestion-id}")
    fun solve(@PathVariable("suggestion-id") suggestionId: Long) {
        val suggestion = suggestionRepository.findByIdOrNull(suggestionId) ?: throw NotFoundException
        val institution = SecurityUtil.getCurrentInstitution()

        val suggestionOfInstitution =
            suggestionOfInstitutionRepository.findBySuggestionIdAndInstitutionId(suggestion.id, institution.id)
                ?: throw NotFoundException

        if (suggestionOfInstitution.status != SuggestionStatus.REGISTERD) {
            throw InvalidStatusException
        }
        suggestionOfInstitution.status = SuggestionStatus.SOLVED
        suggestionOfInstitutionRepository.save(suggestionOfInstitution)
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/suggestion/{suggestion-id}")
    fun delete(@PathVariable("suggestion-id") suggestionId: Long) {
        val suggestion = suggestionRepository.findByIdOrNull(suggestionId) ?: throw NotFoundException
        val institution = SecurityUtil.getCurrentInstitution()
        val suggestionOfInstitution =
            suggestionOfInstitutionRepository.findBySuggestionIdAndInstitutionId(suggestion.id, institution.id)
                ?: throw NotFoundException
        suggestionOfInstitutionRepository.delete(suggestionOfInstitution)
    }

    data class SuggestRequest(
        val title: String,
        val imageUrl: String?,
        val type: SuggestionType,
        val latitude: BigDecimal,
        val longitude: BigDecimal,
        val description: String
    ) {
        fun toSuggestion() = Suggestion(
            title = title,
            imageUrl = imageUrl,
            type = type,
            latitude = latitude,
            longitude = longitude,
            description = description
        )
    }

    @Transactional
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PostMapping("/suggestion")
    fun suggest(@RequestBody request: SuggestRequest) {

        val suggestion = suggestionRepository.save(
            request.toSuggestion()
        )

        suggestionOfInstitutionRepository.saveAll(
            institutionRepository.findAll()
                .mapNotNull {
                    if (it.isContainType(suggestion.type) &&
                        it.isContainPoint(request.latitude, request.longitude))
                    {
                        SuggestionOfInstitution(
                            suggestion = suggestion,
                            institution = it
                        )
                    } else null
                }
        )

        eventPublisher.publishEvent(SuggestionEvent(suggestion))
    }
}