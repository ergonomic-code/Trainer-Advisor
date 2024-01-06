package pro.qyoga.tests.fixture

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import pro.qyoga.platform.file_storage.api.File
import pro.qyoga.tests.fixture.data.imageExtensions
import pro.qyoga.tests.fixture.data.randomFileName
import kotlin.random.Random


object FilesObjectMother {

    fun image(): File {
        val format = imageExtensions.random()
        val data = Random.nextBytes(Random.nextInt(1, 256))
        return File(
            randomFileName { format },
            "image/$format",
            data.size.toLong(),
            data
        )
    }

    fun imageAsMultipartFile(): MultipartFile {
        val img = image()
        return MockMultipartFile(img.name, img.name, img.mediaType, img.data)
    }

}