package pro.qyoga.tests.fixture.data

import net.datafaker.Faker
import java.util.*


fun Faker.randomUUID(): UUID =
    UUID.fromString(faker.internet().uuid())