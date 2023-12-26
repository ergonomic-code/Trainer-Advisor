package pro.qyoga.tests.fixture.therapy.therapeutic_tasks

import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import java.text.Collator
import java.util.*

object SearchTherapeuticTasksFixture {

    const val searchKey = "лор"

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

    private val collator = Collator.getInstance(Locale.of("ru", "RU"))

    fun getExpectedSearchResult(
        allTasks: List<TherapeuticTask>,
        searchKey: String,
        pageSize: Int
    ) =
        allTasks
            .filter { it.name.lowercase().contains(searchKey.lowercase()) }
            .sortedWith { o1, o2 -> collator.compare(o1.name, o2.name) }
            .take(pageSize)
}