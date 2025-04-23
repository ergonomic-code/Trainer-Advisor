package pro.qyoga.tests.cases.app.therapist.clients.journal

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.fixture.backgrounds.ClientsBackgrounds
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryPage
import java.time.LocalDate


@DisplayName("Страница создания записи журнала")
class CreateJournalEntryPageTest : QYogaAppIntegrationBaseTest() {

    private val clientBackgrounds = getBean<ClientsBackgrounds>()

    @Test
    fun `должна рендериться корректно`() {
        // Сетап
        val client = clientBackgrounds.createClients(1, THE_THERAPIST_ID).first()

        // Действие
        val document = theTherapist.clientJournal.getCreateJournalEntryPage(client.id)

        // Проверка
        document shouldBePage CreateJournalEntryPage(client, LocalDate.now())
    }

    @Test
    fun `должна возвращать страницу ошибки 404 при запросе для несуществующего клиента`() {
        // Сетап
        val notExistingClientId = ClientsObjectMother.randomId()

        // Действие
        val document = theTherapist.clientJournal.getCreateJournalEntryPage(
            notExistingClientId,
            expectedStatus = HttpStatus.NOT_FOUND
        )

        // Проверка
        document shouldBePage NotFoundErrorPage
    }

}