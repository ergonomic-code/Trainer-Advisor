package pro.qyoga.tests.fixture.data

import net.datafaker.service.RandomService


fun <T> randomListIndexed(min: Int = 1, max: Int = 10, generator: (Int) -> T) =
    (1..(randomListSize(min, max)))
        .mapIndexed { idx, _ -> generator(idx) }

fun randomListSize(min: Int, max: Int): Int =
    if (min < max)
        faker.random().nextInt(min, max)
    else
        min

fun <T> Collection<T>.randomElement() =
    this.drop(faker.random().nextInt(0, this.size - 1)).first()

fun <T> RandomService.randomElementOf(col: Collection<T>) =
    col.drop(nextInt(0, col.size - 1)).first()