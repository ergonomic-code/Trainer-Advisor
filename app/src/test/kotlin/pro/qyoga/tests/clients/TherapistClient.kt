package pro.qyoga.tests.clients

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.tests.clients.api.*
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD


class TherapistClient(val authCookie: Cookie) {


    // Work
    val appointments = TherapistAppointmentsApi(authCookie)
    val clients = TherapistClientsApi(authCookie)
    val clientJournal = TherapistClientJournalApi(authCookie)
    val clientFiles = TherapistClientFilesApi(authCookie)

    val programs = TherapistProgramsApi(authCookie)

    // Dictionaries
    val exercises = TherapistExercisesApi(authCookie)
    val therapeuticTasks = TherapistTherapeuticTasksApi(authCookie)
    val appointmentTypes = TherapistAppointmentTypesApi(authCookie)

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