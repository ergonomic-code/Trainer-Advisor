package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.app.therapist.therapy.therapeutic_tasks.components.TherapeuticTasksComboBoxController
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.pages.therapist.clients.journal.entry.CreateJournalEntryForm
import pro.qyoga.tests.pages.therapist.clients.journal.entry.TherapeuticTasksSearchResult
import pro.qyoga.tests.pages.therapist.therapy.therapeutic_tasks.TherapeuticTasksListPage

class TherapistTherapeuticTasksApi(override val authCookie: Cookie) : AuthorizedApi {

    fun search(searchKey: String): Document {
        return Given {
            authorized()
            queryParams(TherapeuticTasksListPage.SearchTasksForm.searchKey.name, searchKey)
        } When {
            get(TherapeuticTasksListPage.SearchTasksForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun autocompleteSearch(searchKey: String): Document {
        return Given {
            authorized()
            queryParams(CreateJournalEntryForm.therapeuticTaskNameInput.name, searchKey)
        } When {
            get(TherapeuticTasksSearchResult.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun autocompleteSearch2(searchKey: String?): Document {
        return Given {
            authorized()
            if (searchKey != null) {
                queryParams("therapeuticTaskTitle", searchKey)
            }
            this
        } When {
            get(TherapeuticTasksComboBoxController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getTasksListPage(): Document {
        return Given {
            authorized()
        } When {
            get(TherapeuticTasksListPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun create(task: TherapeuticTask, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()
            formParam(TherapeuticTasksListPage.AddTaskForm.taskName.name, task.name)
        } When {
            post(TherapeuticTasksListPage.path)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun edit(task: TherapeuticTask): Document {
        return Given {
            authorized()
            formParam(TherapeuticTasksListPage.EditTaskForm.taskNameInput.name, task.name)
            pathParam("taskId", task.id)
        } When {
            put(TherapeuticTasksListPage.EditTaskForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun delete(taskId: Long): Document {
        return Given {
            authorized()
            pathParam("taskId", taskId)
        } When {
            delete(TherapeuticTasksListPage.EditTaskForm.action.url)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

}
