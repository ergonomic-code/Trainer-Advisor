package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.app.therapist.therapy.therapeutic_tasks.components.TherapeuticTasksComboBoxController
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.clients.journal.entry.TherapeuticTasksSearchResult
import pro.qyoga.tests.pages.therapist.therapy.programs.EditProgramForm
import pro.qyoga.tests.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


class TherapistTherapeuticTasksApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun search(searchKey: String): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(TherapeuticTasksListPage.SearchTasksForm.action.url).apply {
                    queryParam(TherapeuticTasksListPage.SearchTasksForm.searchKey.name, searchKey)
                }.build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun autocompleteSearch(searchKey: String): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(TherapeuticTasksSearchResult.PATH).apply {
                    queryParam(EditProgramForm.therapeuticTaskInput.name, searchKey)
                }.build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun autocompleteSearch2(searchKey: String?): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(TherapeuticTasksComboBoxController.PATH).apply {
                    if (searchKey != null) {
                        queryParam("therapeuticTaskTitle", searchKey)
                    }
                }.build()
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun getTasksListPage(): Document {
        return restTestClient.get()
            .uri(TherapeuticTasksListPage.path)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun create(task: TherapeuticTask, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.post()
            .uri(TherapeuticTasksListPage.path)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                TherapeuticTasksListPage.AddTaskForm.taskName.name to task.name,
            ))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun edit(task: TherapeuticTask): Document {
        return restTestClient.put()
            .uri(TherapeuticTasksListPage.EditTaskForm.action.url, task.id)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(formData(
                TherapeuticTasksListPage.EditTaskForm.taskNameInput.name to task.name,
            ))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun delete(taskId: Long): Document {
        return restTestClient.delete()
            .uri(TherapeuticTasksListPage.EditTaskForm.action.url, taskId)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

}
