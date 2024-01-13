package pro.qyoga.tests.fixture

import org.springframework.mock.web.MockMultipartFile
import org.springframework.web.multipart.MultipartFile
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.StoredFile
import pro.qyoga.tests.fixture.data.fileName
import pro.qyoga.tests.fixture.data.randomFileBaseName
import pro.qyoga.tests.fixture.data.randomImageExtension
import kotlin.random.Random


object FilesObjectMother {

    fun image(
        name: String = randomFileBaseName(),
        format: String = randomImageExtension()
    ): StoredFile {
        val data = Random.nextBytes(Random.nextInt(1, 256))
        return StoredFile(
            FileMetaData(
                fileName(name, format),
                "image/$format",
                data.size.toLong()
            ),
            data
        )
    }

    fun randomImageAsMultipartFile(
        name: String = randomFileBaseName(),
        format: String = randomImageExtension()
    ): MultipartFile {
        val img = image(name, format)
        return MockMultipartFile(img.metaData.name, img.metaData.name, img.metaData.mediaType, img.content)
    }

}