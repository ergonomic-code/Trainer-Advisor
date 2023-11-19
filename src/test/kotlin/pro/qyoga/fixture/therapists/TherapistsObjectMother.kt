package pro.qyoga.fixture.therapists

import pro.qyoga.core.users.api.RegisterTherapistRequest
import pro.qyoga.fixture.data.randomCyrillicWord
import pro.qyoga.fixture.data.randomEmail


object TherapistsObjectMother {

    fun registerTherapistRequest(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        email: String = randomEmail()
    ) =
        RegisterTherapistRequest(firstName, lastName, email)

}