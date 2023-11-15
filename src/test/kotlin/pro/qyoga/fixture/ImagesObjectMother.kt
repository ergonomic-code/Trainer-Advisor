package pro.qyoga.fixture

import pro.qyoga.fixture.data.imageExtensions
import pro.qyoga.fixture.data.randomFileName
import pro.qyoga.infra.files.api.File
import kotlin.random.Random


object ImagesObjectMother {

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