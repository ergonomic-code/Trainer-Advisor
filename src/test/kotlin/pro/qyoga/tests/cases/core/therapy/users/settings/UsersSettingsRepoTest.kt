package pro.qyoga.tests.cases.core.therapy.users.settings

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.users.settings.UserSettingsRepo
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppBaseTest
import java.time.ZoneId


class UserSettingsRepoTest : QYogaAppBaseTest() {

    @Test
    fun `UserSettingsRepo should return stored timezone when there is appointments`() {
        // Given
        val lastAppointmentTimeZone = ZoneId.of("Europe/Moscow")
        backgrounds.appointments.create(timeZone = lastAppointmentTimeZone)

        // When
        val timeZone = getBean<UserSettingsRepo>().getUserTimeZone(AggregateReference.to(THE_THERAPIST_ID))

        // Then
        // см. коммент к методу репоза
        timeZone shouldBe lastAppointmentTimeZone
    }

}