package pro.qyoga.tests.cases.app.therapist.clients.card

import io.kotest.inspectors.forAny
import io.kotest.matchers.collections.shouldNotBeEmpty
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.azhidkov.platform.extensible_entity.descriptor.CustomFieldType
import pro.azhidkov.platform.spring.sdj.erpo.hydration.ref
import pro.qyoga.core.clients.cards.model.DistributionSource
import pro.qyoga.core.clients.cards.model.DistributionSourceType
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataField
import pro.qyoga.core.clients.therapeutic_data.values.StringTherapeuticDataFieldValue
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.assertions.shouldHaveComponent
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.faker
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother.therapeuticDataBlock
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
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
        val newClientCardForm = ClientsObjectMother.createEditClientCardForm()

        // When
        therapist.clients.createClientCard(newClientCardForm)

        // Then
        val clients = backgrounds.clients.getAllTherapistClients()
        clients.forAny { it shouldMatch newClientCardForm.clientCard }
    }

    @DisplayName("Пустой комментарий источника источника распространения должен быть сохранён как null")
    @Test
    fun persistenceOfNullDistributionSourceComment() {
        // Given
        val therapist = TherapistClient.loginAsTheTherapist()
        val newClientCardForm = ClientsObjectMother.createEditClientCardForm(
            ClientsObjectMother.createClientCardDto(
                distributionSource = DistributionSource(DistributionSourceType.OTHER, null)
            )
        )

        // When
        therapist.clients.createClientCard(newClientCardForm)

        // Then
        val clients = backgrounds.clients.getAllTherapistClients()
        clients.forAny { it shouldMatch newClientCardForm.clientCard }
    }

    @DisplayName("Система должна принимать формы, заполненные только в обязательных полях")
    @Test
    fun minimalClientCreation() {
        // Given
        val minimalEditClientCardForm = ClientsObjectMother.createEditClientCardForm(
            ClientsObjectMother.createClientCardDtoMinimal()
        )
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClientCard(minimalEditClientCardForm)

        // Then
        val clients = backgrounds.clients.getAllTherapistClients()
        clients.forAny { it shouldMatch minimalEditClientCardForm.clientCard }
    }

    @DisplayName("Система должна возвращать форму с индикацией ошибки в поле ввода телефона при отправке формы с уже существующим телефоном")
    @Test
    fun duplicatedPhone() {
        // Given
        val thePhone = faker.phoneNumber().phoneNumberInternational()
        backgrounds.clients.createClient(phone = thePhone)
        val duplicatedEditClientCardForm = ClientsObjectMother.createEditClientCardForm(
            ClientsObjectMother.createClientCardDto(phone = thePhone)
        )
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.clients.createClientForError(duplicatedEditClientCardForm)

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
        val duplicatedEditClientCardForm = ClientsObjectMother.createEditClientCardForm(
            ClientsObjectMother.createClientCardDto(phone = thePhone)
        )
        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        therapist.clients.createClientCard(duplicatedEditClientCardForm)

        // Then
        val clients = backgrounds.clients.getAllTherapistClients()
        clients.forAny { it shouldMatch duplicatedEditClientCardForm.clientCard }
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

    @DisplayName("При наличии пользовательского строковго поля в форме терапевтических данных, оно должно сохраняться в БД при отправке формы")
    @Test
    fun stringCustomFieldPersistence() {
        // Given
        val stringCustomField = TherapeuticDataField("Время подъёма", CustomFieldType.STRING, true)
        val therapeuticDataBlock = therapeuticDataBlock(stringCustomField)
        val therapist = TherapistClient.loginAsTheTherapist()
        val therapeuticDataDescriptor = backgrounds.clients.createTherapeuticDataDescriptor {
            TherapeuticDataDescriptorsObjectMother.therapeuticDataDescriptor(therapeuticDataBlock)
        }
        val customField = therapeuticDataDescriptor.fields[0]
        val customFieldValue = randomCyrillicWord()

        val customFields = listOf(
            stringCustomField to randomCyrillicWord()
        )

        val clientCardDto = ClientsObjectMother.createEditClientCardForm(customFields = customFields)

        // When
        therapist.clients.createClientCard(clientCardDto)

        // Then
        val client = backgrounds.clients.getAllTherapistClients(THE_THERAPIST_REF).single()
        backgrounds.clients.getTherapeuticData(client.ref()).shouldNotBeEmpty()
    }

    @DisplayName("При наличии пользовательского текстового поля в форме терапевтических данных, оно должно отображаться на странице создания клиента")
    @Test
    fun textCustomFieldRendering() {
        io.kotest.assertions.fail("TODO")
    }

}