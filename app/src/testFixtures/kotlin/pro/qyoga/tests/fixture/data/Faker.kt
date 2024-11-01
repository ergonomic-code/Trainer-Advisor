package pro.qyoga.tests.fixture.data

import net.datafaker.Faker
import pro.qyoga.l10n.systemLocale


var faker = Faker(systemLocale, java.util.Random(1))
    private set

fun resetFaker() {
    faker = Faker(systemLocale, java.util.Random(1))
}