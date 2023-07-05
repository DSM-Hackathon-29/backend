package kr.hs.dsm.backend.domain.image.presentation

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.internal.Mimetypes
import com.amazonaws.services.s3.model.CannedAccessControlList
import com.amazonaws.services.s3.model.ObjectMetadata
import com.amazonaws.services.s3.model.PutObjectRequest
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.UUID
import kr.hs.dsm.backend.domain.image.config.AwsS3Properties
import kr.hs.dsm.backend.global.error.InternalServerError
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class ImageController(
    private val awsProperties: AwsS3Properties,
    private val amazonS3Client: AmazonS3Client
) {

    @PostMapping("/image", consumes = [
        MediaType.MULTIPART_FORM_DATA_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.IMAGE_JPEG_VALUE
    ])
    fun uploadImage(@RequestPart("file") multiPartFile: MultipartFile): ImageUrlResponse {
        val file = multiPartFile.toFile()
        inputS3(file, file.name)
        return ImageUrlResponse(
            fileUrl = getResourceUrl(file.name)
        )
    }

    fun MultipartFile.toFile() =
        File("${UUID.randomUUID()}||$originalFilename").let {
            FileOutputStream(it).run {
                this.write(bytes)
                this.close()
            }
            it
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