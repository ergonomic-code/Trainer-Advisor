package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.extensible_entity.descriptor.CustomFieldType
import pro.qyoga.core.clients.cards.model.DistributionSource
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataField
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother.therapeuticDataBlock
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientForm
import pro.qyoga.tests.pages.therapist.clients.card.CreateClientPage
import pro.qyoga.tests.platform.html.custom.TherapeuticDataFormPart

@DisplayName("Страница создания карточки клиента")
class CreateClientPageTest : QYogaAppIntegrationBaseTest() {

    @DisplayName("Страница создания карточки клиента должна содержать все ключевые элементы")
    @Test
    fun pageRendering() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.getCreateClientPage()

        // Then
        document shouldBe CreateClientPage
    }

    @DisplayName("После создания клиента он должен появиться в списке клиентов")
    @Test
    fun clientCreation() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = ClientsObjectMother.createClientCardDto()

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch newClientRequest }
    }

    @DisplayName("Пустой комментарий источника источника распространения должен быть сохранён как null")
    @Test
    fun persistenceOfNullDistributionSourceComment() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientRequest = ClientsObjectMother.createClientCardDto(
            distributionSource = DistributionSource(DistributionSourceType.OTHER, null)
        )

        // When
        therapist.clients.createClient(newClientRequest)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch newClientRequest }
    }

    @DisplayName("Система должна принимать формы, заполненные только в обязательных полях")
    @Test
    fun minimalClientCreation() {
        // Given
        val minimalClient = ClientsObjectMother.createClientCardDtoMinimal()
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClient(minimalClient)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch minimalClient }
    }

    @DisplayName("Система должна возвращать форму с индикацией ошибки в поле ввода телефона при отправке формы с уже существующим телефоном")
    @Test
    fun duplicatedPhone() {
        // Given
        val thePhone = faker.phoneNumber().phoneNumberInternational()
        backgrounds.clients.createClient(phone = thePhone)
        val duplicatedClient = ClientsObjectMother.createClientCardDto(phone = thePhone)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.createClientForError(duplicatedClient)

        // Then
        document shouldHaveElement CreateClientForm.invalidPhoneInput
    }

    @DisplayName("Система должна позволять создавать клиента с номером телефона, уже существующего у другого терапевта")
    @Test
    fun multiplePhoneNumbersForDifferentTherapists() {
        // Given
        val thePhone = faker.phoneNumber().phoneNumberInternational()
        val anotherTherapistId = backgrounds.users.registerNewTherapist().id
        backgrounds.clients.createClient(phone = thePhone, therapistId = anotherTherapistId)
        val duplicatedClient = ClientsObjectMother.createClientCardDto(phone = thePhone)
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClient(duplicatedClient)

        // Then
        val clients = backgrounds.clients.getAllClients()
        clients.forAny { it shouldMatch duplicatedClient }
    }

    @DisplayName("При наличии пользовательского строкового поля в форме терапевтических данных, оно должно отображаться на странице создания клиента")
    @Test
    fun stringCustomFieldRendering() {
        // Given
        val stringCustomField = TherapeuticDataField("Время подъёма", CustomFieldType.STRING, true)
        val therapeuticDataBlock = therapeuticDataBlock(stringCustomField)
        val therapist = TherapistClient.loginAsTheTherapist()

        val therapeuticDataDescriptor = backgrounds.clients.createTherapeuticDataDescriptor {
            TherapeuticDataDescriptorsObjectMother.therapeuticDataDescriptor(therapeuticDataBlock)
        }

        // When
        val document = therapist.clients.getCreateClientPage()

        // Then
        document.getElementById(CreateClientForm.id)!! shouldHaveComponent TherapeuticDataFormPart(
            therapeuticDataDescriptor
        )
    }

    @DisplayName("При наличии пользовательского текстового поля в форме терапевтических данных, оно должно отображаться на странице создания клиента")
    @Test
    fun textCustomFieldRendering() {
        io.kotest.assertions.fail("TODO")
    }

}