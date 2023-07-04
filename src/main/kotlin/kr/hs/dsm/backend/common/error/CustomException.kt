package kr.hs.dsm.backend.common.error

abstract class CustomException(
    val errorProperty: ErrorProperty
) : RuntimeException()
