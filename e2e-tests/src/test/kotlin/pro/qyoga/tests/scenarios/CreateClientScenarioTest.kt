package pro.qyoga.tests.scenarios

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.clients.card.shouldBeEmpty
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.infra.QYogaE2EBaseTest
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.platform.selenide.click
import pro.qyoga.tests.scripts.loginAsTheTherapist
import pro.qyoga.tests.scripts.therapist.clients.card.fillClientForm
import pro.qyoga.tests.scripts.therapist.clients.card.goToCreateClientPage


@DisplayName("Созданиe клиента")
class CreateClientScenarioTest : QYogaE2EBaseTest() {

    @DisplayName("Успешное создание")
    @Test
    fun successfulClientCreation() {
        // Фикстура
        val aClient = createClientCardDto()
        loginAsTheTherapist()

        // Терапевт переходит на страницу создания клиента
        goToCreateClientPage()

        // И видит пустую форму
        CreateClientForm.shouldBeEmpty()

        // Затем заполняет и отправляет её
        fillClientForm(aClient)
        click(CreateClientForm.submit)

        // После чего клиент появляется в списке
        backgrounds.clients.getAllTherapistClients().forAny {
            it shouldMatch aClient
        }

        // Затем терапевт снова переходит на страницу создания клиента
        goToCreateClientPage()

        // И снова видит постую форму - черновик был очищен после отправки формы
        CreateClientForm.shouldBeEmpty()
    }

}