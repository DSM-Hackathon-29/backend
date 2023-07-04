package kr.hs.dsm.backend.global.error

import kr.hs.dsm.backend.common.error.CustomException

object InternalServerError : CustomException(
    GlobalErrorCode.INTERNAL_SERVER_ERROR
)

object PasswordMismatchException : CustomException(
    GlobalErrorCode.PASSWORD_MISMATCH
)

object NotFoundException : CustomException(
    GlobalErrorCode.NOT_FOUND
)

object InvalidStatusException : CustomException(
    GlobalErrorCode.INVALID_STATUS
)
