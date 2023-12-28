package pro.qyoga.tests.assertions

import io.kotest.matchers.date.shouldBeCloseTo
import io.kotest.matchers.equality.FieldsEqualityCheckConfig
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.shouldNotBe
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import kotlin.time.Duration.Companion.milliseconds


private val ignoredFields = listOf(
    TherapeuticTask::createdAt, // после сохранения пропадают миллионные наносекунд
    TherapeuticTask::lastModifiedAt,
    TherapeuticTask::version,
)

infix fun TherapeuticTask?.shouldMatch(therapeuticTask: TherapeuticTask) {
    this shouldNotBe null
    this!!.shouldBeEqualToComparingFields(
        therapeuticTask,
        FieldsEqualityCheckConfig(propertiesToExclude = ignoredFields)
    )
    this.createdAt.shouldBeCloseTo(therapeuticTask.createdAt, 1.milliseconds)
}