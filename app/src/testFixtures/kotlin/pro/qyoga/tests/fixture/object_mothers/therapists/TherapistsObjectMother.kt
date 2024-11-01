package pro.qyoga.tests.fixture.object_mothers.therapists

import pro.qyoga.core.users.therapists.RegisterTherapistRequest
import pro.qyoga.tech.captcha.CaptchaAnswer
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.data.randomEmail
import java.util.*


object TherapistsObjectMother {

    fun registerTherapistRequest(
        firstName: String = randomCyrillicWord(),
        lastName: String = randomCyrillicWord(),
        email: String = randomEmail(),
        captchaAnswer: CaptchaAnswer = captchaAnswer()
    ) =
        RegisterTherapistRequest(firstName, lastName, email, captchaAnswer)

    fun captchaAnswer(
        captchaId: UUID = UUID.randomUUID(),
        captchaCode: String = faker.text().text(7)
    ) =
        CaptchaAnswer(captchaId, captchaCode)
}