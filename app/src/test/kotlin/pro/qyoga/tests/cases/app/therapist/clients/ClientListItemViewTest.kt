package pro.qyoga.tests.cases.app.therapist.clients

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother
import java.time.LocalDate


@DisplayName("Модель представления строки списка клиентов")
class ClientListItemViewTest : FreeSpec({

    "для клиента с записью от 16 марта 2025" - {
        val lastEntryDate = LocalDate.of(2025, 3, 16)
        val aClientListItemView = ClientsObjectMother.aClientListItemView(lastEntryDate = lastEntryDate)

        "должна рендерить лейбл даты последней записи в виде короткой даты при запросе 15 апреля 2025 (менее месяца назад)" {
            val now = LocalDate.of(2025, 4, 15)
            aClientListItemView.lastJournalEntryDateLabel(now) shouldBe "16.03"
        }

        "должна рендерить лейбл даты последней записи в виде интервала при запросе 17 апреля 2025 (более месяца назад)" {
            val now = LocalDate.of(2025, 4, 17)
            aClientListItemView.lastJournalEntryDateLabel(now) shouldBe "Более месяца назад"
        }

    }

})