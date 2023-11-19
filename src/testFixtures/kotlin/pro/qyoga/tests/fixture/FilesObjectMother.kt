package pro.qyoga.tests.fixture

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.tests.fixture.data.imageExtensions
import pro.qyoga.tests.fixture.data.randomFileName
import kotlin.random.Random


object FilesObjectMother {

    fun image(): StoredFile {
        val format = imageExtensions.random()
        val data = Random.nextBytes(Random.nextInt(1, 256))
        return StoredFile(
            FileMetaData(
                randomFileName { format },
                "image/$format",
                data.size.toLong()
            ),
            data
        )
    }

    fun imageAsMultipartFile(): MultipartFile {
        val img = image()
        return MockMultipartFile(img.metaData.name, img.metaData.name, img.metaData.mediaType, img.content)
    }

}