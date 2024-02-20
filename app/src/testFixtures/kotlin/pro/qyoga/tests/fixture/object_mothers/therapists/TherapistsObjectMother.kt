package pro.qyoga.tests.fixture.object_mothers.therapists

import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomEmail


object TherapistsObjectMother {

    fun registerTherapistRequest(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        email: String = randomEmail()
    ) =
        RegisterTherapistRequest(firstName, lastName, email)

}