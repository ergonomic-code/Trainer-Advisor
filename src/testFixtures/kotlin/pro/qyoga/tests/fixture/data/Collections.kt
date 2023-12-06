package pro.qyoga.tests.fixture.data


fun <T> randomListIndexed(min: Int = 1, max: Int = 10, generator: (Int) -> T) =
    (min..max).mapIndexed { idx, _ -> generator(idx) }
