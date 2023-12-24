package pro.qyoga.tests.clients

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.tests.clients.api.TherapistClientJournalApi
import pro.qyoga.tests.clients.api.TherapistClientsApi
import pro.qyoga.tests.clients.api.TherapistExercisesApi
import pro.qyoga.tests.clients.api.TherapistTherapeuticTasksApi
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_PASSWORD


class TherapistClient(val authCookie: Cookie) {

    val clients = TherapistClientsApi(authCookie)
    val clientJournal = TherapistClientJournalApi(authCookie)
    val exercises = TherapistExercisesApi(authCookie)
    val therapeuticTasks = TherapistTherapeuticTasksApi(authCookie)

    fun getIndexPage(): Document {
        return Given {
            cookie(authCookie)
        } When {
            get()
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    companion object {

        fun loginAsTheTherapist(): TherapistClient =
            login(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)

        fun login(email: String, password: String): TherapistClient =
            TherapistClient(
                PublicClient.authApi.login(email, password)
            )

    }

}