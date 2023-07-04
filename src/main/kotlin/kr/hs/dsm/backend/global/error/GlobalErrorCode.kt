package kr.hs.dsm.backend.global.error

import kr.hs.dsm.backend.common.error.ErrorProperty
import kr.hs.dsm.backend.common.error.ErrorStatus

enum class GlobalErrorCode(
    private val status: Int,
    private val message: String,
    private val sequence: Int
) : ErrorProperty {

    BAD_REQUEST(ErrorStatus.BAD_REQUEST, "Bad request", 1),
    INVALID_STATUS(ErrorStatus.BAD_REQUEST, "Invalid status", 2),

    PASSWORD_MISMATCH(ErrorStatus.FORBIDDEN, "Password mismatched", 1),
    NOT_FOUND(ErrorStatus.NOT_FOUND, "Not found", 1),

    INTERNAL_SERVER_ERROR(ErrorStatus.INTERNAL_SERVER_ERROR, "Internal server error", 1)
    ;

    override fun status(): Int = status
    override fun message(): String = message
    override fun code(): String = "EXCEPTION-$status-$sequence"
}
