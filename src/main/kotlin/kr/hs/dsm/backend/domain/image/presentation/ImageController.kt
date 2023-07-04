package kr.hs.dsm.backend.domain.image.presentation

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.internal.Mimetypes
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.File
import java.io.IOException
import kr.hs.dsm.backend.domain.image.config.AwsS3Properties
import kr.hs.dsm.backend.global.error.InternalServerError
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class ImageController(
    private val awsProperties: AwsS3Properties,
    private val amazonS3Client: AmazonS3Client
) {

    @PostMapping("/image")
    fun uploadImage(file: File): ImageUrlResponse {
        inputS3(file, file.name)
        return ImageUrlResponse(
            fileUrl = getResourceUrl(file.name)
        )
    }

    private fun inputS3(file: File, fileName: String) {
        try {
            val inputStream = file.inputStream()
            val objectMetadata = ObjectMetadata().apply {
                this.contentLength = file.length()
                this.contentType = Mimetypes.getInstance().getMimetype(file)
            }

            amazonS3Client.putObject(
                PutObjectRequest(awsProperties.bucket, fileName, inputStream, objectMetadata)
            )

            file.delete()
        } catch (e: IOException) {
            throw InternalServerError
        }
    }

    fun getResourceUrl(fileName: String): String {
        return amazonS3Client.getResourceUrl(awsProperties.bucket, fileName)
    }
}

data class ImageUrlResponse(
    val fileUrl: String
)