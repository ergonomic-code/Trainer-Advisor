package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import io.kotest.inspectors.forNone
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.core.clients.cards.model.PhoneNumber
import pro.qyoga.core.clients.cards.model.toUIFormat
import pro.qyoga.core.clients.cards.toDto
import pro.qyoga.tests.assertions.*
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother.createClientCardDto
import pro.qyoga.tests.fixture.object_mothers.clients.randomPhoneNumber
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientForm
import pro.qyoga.tests.pages.therapist.clients.card.EditClientPage


@DisplayName("Страница редактирования карточки клиента")
class EditClientCardPageTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `должна корректно рендериться`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(1, THE_THERAPIST_ID).first()

        // Действие
        val document = therapist.clients.getEditClientCardPage(client.id)

        // Проверка
        document shouldBe EditClientPage.pageFor(client)
    }

    @Test
    fun `должна корректно рендериться для клиента с заполненными только обязательными полями`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val client = backgrounds.clients.createClients(listOf(ClientsObjectMother.createClientCardDtoMinimal())).first()

        // Действие
        val document = therapist.clients.getEditClientCardPage(client.id)

        // Проверка
        document shouldBe EditClientPage.pageFor(client)
    }

    @Test
    fun `должна сохранять изменения`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()

        val newClientCardDto = createClientCardDto()
        val client = backgrounds.clients.createClients(listOf(newClientCardDto), THE_THERAPIST_ID).first()

        val editedClientCardDto = createClientCardDto()

        // Действие
        therapist.clients.editClient(client.id, editedClientCardDto)

        // Проверка
        val clients = backgrounds.clients.getAllClients()
        clients.forNone { it shouldMatch newClientCardDto }
        clients.forAny { it shouldMatch editedClientCardDto }
    }

    @Test
    fun `должна сохранять изменения, если указаны только значения обязательных полей`() {
        // Сетап
        val minimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val editedMinimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val minimalClientId = backgrounds.clients.createClients(listOf(minimalClient), THE_THERAPIST_ID).single().id
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        therapist.clients.editClient(minimalClientId, editedMinimalClient)

        // Проверка
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch editedMinimalClient }
    }

    @Test
    fun `при запросе для не существующего клиента должна возвращать стандартную страницу ошибки 404`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()

        // Действие
        val document =
            therapist.clients.getEditClientCardPage(notExistingClientId, expectedStatus = HttpStatus.NOT_FOUND)

        // Проверка
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `при редактировании несуществующего клиента должна возвращать стандартную страницу ошибки 404`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()
        val notExistingClientId = ClientsObjectMother.randomId()

        // Действие
        val document =
            therapist.clients.editClientForError(
                notExistingClientId,
                createClientCardDto(),
                expectedStatus = HttpStatus.NOT_FOUND
            )

        // Проверка
        document shouldBePage NotFoundErrorPage
    }

    @Test
    fun `должна возвращать форму с сообщением об ошибки дублирования номера телефона при отравке формы с телефоном другого клиента того же терапевта`() {
        // Сетап
        val thePhone = randomPhoneNumber().toUIFormat()
        backgrounds.clients.createClient(phone = thePhone)
        val existingClient = backgrounds.clients.createClient()
        val existingClientDto = existingClient.toDto()
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document =
            therapist.clients.editClientForError(existingClient.id, existingClientDto.copy(phoneNumber = thePhone))

        // Проверка
        document shouldBeElement EditClientForm.clientForm(existingClient.copy(phoneNumber = PhoneNumber.of(thePhone)))
        document shouldHaveElement CreateClientForm.invalidPhoneInput
    }

    @Test
    fun `должна сохранять телефон, указанный для другого клиента другого терапевта`() {
        // Сетап
        val thePhone = randomPhoneNumber().toUIFormat()
        val anotherTherapistId = backgrounds.users.registerNewTherapist().id
        backgrounds.clients.createClient(phone = thePhone, therapistId = anotherTherapistId)
        val targetClient = backgrounds.clients.createClient()
        val updatePhoneDto = targetClient.toDto().copy(phoneNumber = thePhone)
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        therapist.clients.editClient(targetClient.id, updatePhoneDto)

        // Проверка
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch updatePhoneDto }
    }

}