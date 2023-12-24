package pro.qyoga.tests.cases.app.therapist.clients.journal

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.clients.pages.therapist.clients.journal.entry.TherapeuticTasksSearchResult
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import java.text.Collator
import java.util.*


class SearchTherapeuticTaskTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Search of therapeutic task should return 5 tasks containing search key in any part of name in any case`() {
        // Given
        val searchKey = "лор"

        val matchingTaskNames = listOf(
            "лорМобилизация ПОП",
            "Лор Снятие компрессии с ПОП",
            "лор Коррекция кифолордотической осанки",
            "ЙТЛоргастрита",
            "ЙТ травмы лор медиального мениска",
            "Коррекция С-образного сколеозаЛор",
            "Коррекция жёсткости ГОП лор"
        )

        val notMatchingTaskNames = listOf(
            "Коррекция протракции головы",
            "Деротация ГОП",
            "Терапия ГЭРБ"
        )

        val therapeuticTasks = backgrounds.therapeuticTasks.createTherapeuticTasks(
            THE_THERAPIST_ID,
            matchingTaskNames + notMatchingTaskNames
        )
        val collator = Collator.getInstance(Locale.of("ru", "RU"))
        val expectedSearchResult = therapeuticTasks
            .filter { it.name.lowercase().contains(searchKey.lowercase()) }
            .sortedWith { o1, o2 -> collator.compare(o1.name, o2.name) }
            .take(5)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.search(searchKey)

        // Then
        document shouldBe TherapeuticTasksSearchResult.componentFor(expectedSearchResult)
    }

}