package kr.hs.dsm.backend.global.security

import org.springframework.boot.context.properties.ConfigurationProperties
import java.util.Base64
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "secret")
class SecurityProperties(
    secretKey: String,
    val accessExp: Int
) {
    val secretKey: String = Base64.getEncoder().encodeToString(secretKey.toByteArray())
}
