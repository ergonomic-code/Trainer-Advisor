package pro.qyoga.tests.cases.app.publc

import com.icegreen.greenmail.junit5.GreenMailExtension
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.app.publc.register.RegisterPageController
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother.defaultTherapeuticDataDescriptor
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother.registerTherapistRequest
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


@DisplayName("Контроллер - Регистрация терапевта")
class RegistrationPageControllerTest : QYogaAppIntegrationBaseTest() {

    private val registerPageController = getBean<RegisterPageController>()

    @DisplayName("После регистрации нового терапевта для него должна автоматически создаваться стандартный дескриптор терапевтических данных")
    @Test
    fun setupOfDefaultTherapeuticFrom() {
        // Given
        val registerTherapistRequest = registerTherapistRequest()

        // When
        registerPageController.register(registerTherapistRequest)

        // Then
        val newTherapist = backgrounds.users.findTherapist(registerTherapistRequest.email)!!
        val therapeuticData = backgrounds.clients.getTherapeuticDataDescription(newTherapist.id)

        therapeuticData shouldMatch defaultTherapeuticDataDescriptor.withOwner(newTherapist.ref())
    }

    companion object {
        @JvmStatic
        @RegisterExtension
        val greenMail: GreenMailExtension = pro.qyoga.tests.infra.green_mail.greenMail
    }

}