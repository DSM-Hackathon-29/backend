package kr.hs.dsm.backend.domain.institution.presentation

import java.math.BigDecimal
import kr.hs.dsm.backend.common.util.SecurityUtil
import kr.hs.dsm.backend.common.dto.TokenResponse
import kr.hs.dsm.backend.domain.institution.persistence.InstitutionRepository
import kr.hs.dsm.backend.domain.institution.persistence.RangePoint
import kr.hs.dsm.backend.domain.institution.persistence.RangePointsRepository
import kr.hs.dsm.backend.domain.institution.persistence.SuggestionTypeOfInstitutionRepository
import kr.hs.dsm.backend.domain.suggestion.enums.SuggestionType
import kr.hs.dsm.backend.global.error.NotFoundException
import kr.hs.dsm.backend.global.error.PasswordMismatchException
import kr.hs.dsm.backend.global.security.token.JwtGenerator
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class InstitutionController(
    private val institutionRepository: InstitutionRepository,
    private val rangePointsRepository: RangePointsRepository,
    private val suggestionTypeOfInstitutionRepository: SuggestionTypeOfInstitutionRepository,
    private val jwtGenerator: JwtGenerator
) {

    data class LoginRequest(
        val accountId: String,
        val password: String
    )

    @Transactional
    @PostMapping("/auth/tokens")
    fun login(@RequestBody request: LoginRequest): TokenResponse {
        val institution = institutionRepository.findByAccountId(request.accountId) ?: throw NotFoundException
        if (institution.password != request.password) {
            throw PasswordMismatchException
        }
        return jwtGenerator.receiveToken(institution.id)
    }

    data class UpdateInstitutionInfoRequest(
        val name: String,
        val suggestionType: List<SuggestionType>,
        val rangePoints: List<RangePointRequest>
    ) {
        data class RangePointRequest(
            val latitude: BigDecimal,
            val longitude: BigDecimal
        )
    }

    @Transactional
    @PatchMapping("/institution")
    fun updateInstitutionInfo(@RequestBody request: UpdateInstitutionInfoRequest) {
        val institution = SecurityUtil.getCurrentInstitution().apply {
            institutionName = request.name
        }
        rangePointsRepository.deleteByInstitutionId(institution.id)
        suggestionTypeOfInstitutionRepository.deleteByInstitutionId(institution.id)

        institutionRepository.save(institution)
        rangePointsRepository.saveAll(
            request.rangePoints.map {
                RangePoint(
                    latitude = it.latitude,
                    longitude = it.longitude,
                    institution = institution
                )
            }
        )
        suggestionTypeOfInstitutionRepository.saveAll(
            request.suggestionType.map {
                kr.hs.dsm.backend.domain.institution.persistence.SuggestionTypeOfInstitution(
                    type = it,
                    institution = institution
                )
            }
        )
    }

    data class GetInstitutionInfoResponse(
        val name: String,
        val rangePoints: List<RangePointResponse>
    ) {
        data class RangePointResponse(
            val latitude: BigDecimal,
            val longitude: BigDecimal
        )
    }

    @Transactional(readOnly = true)
    @GetMapping("/institution")
    fun getInstitutionInfo(): GetInstitutionInfoResponse {
        val institution = SecurityUtil.getCurrentInstitution()
        val rangePoints = rangePointsRepository.findByInstitutionId(institution.id)

        return GetInstitutionInfoResponse(
            name = institution.institutionName,
            rangePoints = rangePoints.map { GetInstitutionInfoResponse.RangePointResponse(
                latitude = it.latitude,
                longitude = it.longitude
            ) }
        )
    }
}