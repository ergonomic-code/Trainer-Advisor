package pro.qyoga.fixture

import pro.qyoga.fixture.data.imageExtensions
import pro.qyoga.fixture.data.randomFileName
import pro.qyoga.infra.images.api.Image
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