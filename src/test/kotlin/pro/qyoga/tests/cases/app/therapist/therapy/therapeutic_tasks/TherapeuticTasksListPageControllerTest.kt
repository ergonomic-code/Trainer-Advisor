package pro.qyoga.tests.cases.app.therapist.therapy.therapeutic_tasks

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.Test
import pro.qyoga.app.therapist.therapy.therapeutic_tasks.DUPLICATED_NEW_TASK_NAME
import pro.qyoga.app.therapist.therapy.therapeutic_tasks.TherapeuticTasksListsPageController
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapists.theTherapistUserDetails
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest

class TherapeuticTasksListPageControllerTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Task name case should not be considering in task duplication`() {
        // Given
        val taskName = "ЙТ гастрита"
        val duplicateWithDifferentCase = taskName.lowercase()
        backgrounds.therapeuticTasks.createTherapeuticTask(THE_THERAPIST_ID, taskName)

        // When
        val res =
            getBean<TherapeuticTasksListsPageController>().create(duplicateWithDifferentCase, theTherapistUserDetails)

        // Then
        res.model[DUPLICATED_NEW_TASK_NAME] shouldBe true
    }

}