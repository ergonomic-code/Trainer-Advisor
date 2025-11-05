package pro.qyoga.tests.fixture.test_apis

import org.springframework.stereotype.Component
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomEditAppointmentRequest
import pro.qyoga.tests.fixture.presets.AppointmentsFixturePresets
import java.time.LocalDateTime
import java.time.ZoneId


@Component
class UserTimeZonesTestApi(
    private val appointmentsFixturePresets: AppointmentsFixturePresets
) {

    fun setTimeZone(therapistRef: TherapistRef, timeZone: ZoneId) {
        val randomEditAppointmentRequest = randomEditAppointmentRequest(
            dateTime = LocalDateTime.of(2001, 2, 3, faker.random().nextInt(1, 23), faker.random().nextInt(1, 59)),
            timeZone = timeZone,
        )
        appointmentsFixturePresets.createAppointment(
            therapistRef = therapistRef,
            editAppointmentRequest = randomEditAppointmentRequest,
        )
    }

}
