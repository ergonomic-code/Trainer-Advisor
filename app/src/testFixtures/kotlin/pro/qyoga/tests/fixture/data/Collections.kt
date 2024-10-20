package pro.qyoga.tests.fixture.data


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