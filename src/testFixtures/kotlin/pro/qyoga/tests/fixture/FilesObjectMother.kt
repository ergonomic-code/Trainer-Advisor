package pro.qyoga.tests.fixture

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

}