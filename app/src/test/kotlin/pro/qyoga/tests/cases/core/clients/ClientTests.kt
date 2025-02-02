package pro.qyoga.tests.cases.core.clients

import io.kotest.core.annotation.DisplayName
import io.kotest.core.spec.style.FreeSpec
import io.kotest.matchers.shouldBe
import pro.qyoga.tests.fixture.object_mothers.clients.ClientsObjectMother


@DisplayName("Модель клиента")
class ClientTests : FreeSpec({

    "метод добавления жалоб" - {

        "при добавлении жалобы к значению пустой строки" - {
            // Сетап
            val client = ClientsObjectMother.aClientMinimal(complains = "")
            val complaints = "Новая жалоба"

            // Действие
            val result = client.prependComplaints(complaints)

            // Проверки
            "не должен добавлять перевод строки в конец" {
                result.complaints shouldBe "Новая жалоба"
            }
        }

    }

})