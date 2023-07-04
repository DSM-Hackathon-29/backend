package kr.hs.dsm.backend.global.security.exception

import kr.hs.dsm.backend.common.error.CustomException
import kr.hs.dsm.backend.global.security.exception.error.SecurityErrorCode

object ExpiredTokenException : CustomException(
    SecurityErrorCode.EXPIRED_TOKEN
)
