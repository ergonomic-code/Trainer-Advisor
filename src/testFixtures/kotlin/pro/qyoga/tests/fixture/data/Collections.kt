package pro.qyoga.tests.fixture.data

import kotlin.random.Random


fun <T> randomListIndexed(min: Int = 1, max: Int = 10, generator: (Int) -> T) =
    (1..(randomListSize(min, max)))
        .mapIndexed { idx, _ -> generator(idx) }

fun randomListSize(min: Int, max: Int) =
    if (min < max)
        Random.nextInt(min, max)
    else
        min
