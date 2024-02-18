package pro.qyoga.tests.cases.app.therapist.clients.journal

import org.junit.jupiter.api.Test
import pro.qyoga.tests.assertions.shouldBe
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.pages.therapist.clients.journal.entry.TherapeuticTasksSearchResult
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks.SearchTherapeuticTasksFixture
import pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks.SearchTherapeuticTasksFixture.matchingTaskNames
import pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks.SearchTherapeuticTasksFixture.notMatchingTaskNames
import pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks.SearchTherapeuticTasksFixture.searchKey
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


class SearchTherapeuticTaskTest : QYogaAppIntegrationBaseTest() {

    @Test
    fun `Search of therapeutic task should return 5 tasks containing search key in any part of name in any case`() {
        // Given
        val therapeuticTasks = backgrounds.therapeuticTasks.createTherapeuticTasks(
            THE_THERAPIST_ID, matchingTaskNames + notMatchingTaskNames
        )
        val expectedSearchResult = SearchTherapeuticTasksFixture.getExpectedSearchResult(therapeuticTasks, searchKey, 5)

        val therapist = TherapistClient.loginAsTheTherapist()

        // When
        val document = therapist.therapeuticTasks.autocompleteSearch(searchKey)

        // Then
        document shouldBe TherapeuticTasksSearchResult.componentFor(expectedSearchResult)
    }

}
