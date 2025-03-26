package pro.qyoga.tests.fixture.presets

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration


@TestConfiguration
@ComponentScan
@Configuration
class Presets(
    val surveysFixturePresets: SurveysFixturePresets,
    val therapistsFixturePreset: TherapistsFixturePreset,
    val clientsFixturePresets: ClientsFixturePresets,
    val calendarsFixturePresets: CalendarsFixturePresets
)