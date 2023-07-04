package kr.hs.dsm.backend.global.security.exception

import kr.hs.dsm.backend.common.error.CustomException
import kr.hs.dsm.backend.global.security.exception.error.SecurityErrorCode

object InvalidTokenException : CustomException(
    SecurityErrorCode.INVALID_TOKEN
)
