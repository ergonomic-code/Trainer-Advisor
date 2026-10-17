package pro.qyoga.tests.clients

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import pro.qyoga.tests.clients.api.*
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_LOGIN
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_PASSWORD
import pro.qyoga.tests.infra.web.mainRestTestClient
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString


class TherapistClient(
    val authCookie: HttpCookie,
    restTestClient: RestTestClient = mainRestTestClient
) {

    // Work
    val appointments = TherapistAppointmentsApi(authCookie)
    val googleCalendarIntegration = TherapistGoogleCalendarIntegrationApi(authCookie, restTestClient)
    val clients = TherapistClientsApi(authCookie)
    val clientJournal = TherapistClientJournalApi(authCookie)
    val clientFiles = TherapistClientFilesApi(authCookie)

    val programs = TherapistProgramsApi(authCookie)

    // Dictionaries
    val exercises = TherapistExercisesApi(authCookie)
    val therapeuticTasks = TherapistTherapeuticTasksApi(authCookie)
    val appointmentTypes = TherapistAppointmentTypesApi(authCookie)

    // Account
    val settings = TherapistSettingsApi(authCookie)

    fun getIndexPage(): Document {
        return pageRestTestClient.get()
            .uri("/therapist")
            .accept(MediaType.TEXT_HTML)
            .cookie(authCookie.name, authCookie.value)
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    companion object {

        fun loginAsTheTherapist(): TherapistClient {
            return login(THE_THERAPIST_LOGIN, THE_THERAPIST_PASSWORD)
        }

        fun login(email: String, password: String): TherapistClient {
            return TherapistClient(PublicClient.authApi.login(email, password))
        }

    }

}
