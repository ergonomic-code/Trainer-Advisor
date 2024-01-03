package pro.qyoga.tests.fixture

import pro.qyoga.platform.file_storage.api.Image
import pro.qyoga.tests.fixture.data.imageExtensions
import pro.qyoga.tests.fixture.data.randomFileName
import kotlin.random.Random


object ImagesObjectMother {

    fun image(): Image {
        val format = imageExtensions.random()
        val data = Random.nextBytes(Random.nextInt(1, 256))
        return Image(
            randomFileName { format },
            "image/$format",
            data.size.toLong(),
            data
        )
    }

}