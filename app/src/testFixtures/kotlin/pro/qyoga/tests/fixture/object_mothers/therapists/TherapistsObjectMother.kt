package pro.qyoga.tests.fixture.object_mothers.therapists

import pro.azhidkov.platform.spring.sdj.erpo.hydration.AggregateReferenceTarget
import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomEmail


object TherapistsObjectMother {

    fun registerTherapistRequest(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        email: String = randomEmail()
    ) =
        RegisterTherapistRequest(firstName, lastName, email)

    fun therapist() = Therapist(
        faker.name().firstName(),
        faker.name().lastName(),
        -1
    )

    val fakeTherapistRef: TherapistRef = AggregateReferenceTarget(
        therapist()
    )

}