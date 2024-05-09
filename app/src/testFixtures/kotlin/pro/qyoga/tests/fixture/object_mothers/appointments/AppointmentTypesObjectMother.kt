package pro.qyoga.tests.fixture.object_mothers.appointments

import pro.azhidkov.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF


object AppointmentTypesObjectMother {

    fun randomAppointmentType(
        therapistRef: TherapistRef = THE_THERAPIST_REF,
        name: String = randomCyrillicWord()
    ) = AppointmentType(therapistRef, name)

    fun fakeAppointmentType() = AggregateReferenceTarget(
        AppointmentType(THE_THERAPIST_REF, "Тренировка", -1)
    )

}
