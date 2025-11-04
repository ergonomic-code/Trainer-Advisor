package pro.qyoga.tests.scenarios

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.tests.assertions.clients.card.currentPageShouldBeFormFor
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.infra.QYogaE2EBaseTest
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage
import pro.qyoga.tests.platform.selenide.open
import pro.qyoga.tests.scripts.loginAsTheTherapist


@DisplayName("Страница редактирования клиента")
class EditClientPageTest : QYogaE2EBaseTest() {

    @Test
    fun `должна корректно рендерится`() {
        // Сетап
        val client = backgrounds.clients.createClient(clientDto = ClientsObjectMother.createClientCardDto())
        loginAsTheTherapist()

        // Действие
        open(EditClientPage.forClient(client.ref()))

        // Проверка
        currentPageShouldBeFormFor(client)
    }

}
