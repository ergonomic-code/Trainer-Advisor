package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.tests.clients.pages.therapist.appointments.CreateAppointmentPage
import pro.qyoga.tests.clients.pages.therapist.appointments.SchedulePage
import java.time.format.DateTimeFormatter

class TherapistAppointmentsApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getTrainingSchedulePage(): Document {
        return Given {
            authorized()
        } When {
            get(SchedulePage.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getCreateAppointmentPage(): Document {
        return Given {
            authorized()

        } When {
            get(CreateAppointmentPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createAppointment(appointment: EditAppointmentRequest): Response {
        return Given {
            authorized()

            formParam(CreateAppointmentPage.editAppointmentForm.clientInput.name, appointment.client.id)

            formParam(CreateAppointmentPage.editAppointmentForm.typeInput.name, appointment.appointmentType)
            formParam(
                CreateAppointmentPage.editAppointmentForm.typeInput.titleInputId,
                appointment.appointmentTypeTitle
            )

            formParam(
                CreateAppointmentPage.editAppointmentForm.therapeuticTaskInput.name,
                appointment.therapeuticTask.id
            )

            formParam(
                CreateAppointmentPage.editAppointmentForm.dateTime.name,
                appointment.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
            )
            formParam(
                CreateAppointmentPage.editAppointmentForm.timeZone.name,
                appointment.timeZone.id
            )
            formParam(CreateAppointmentPage.editAppointmentForm.place.name, appointment.place ?: "")

            formParam(CreateAppointmentPage.editAppointmentForm.cost.name, appointment.cost ?: "")
            formParam(CreateAppointmentPage.editAppointmentForm.payed.name, appointment.payed ?: "")

            formParam(CreateAppointmentPage.editAppointmentForm.comment.name, appointment.comment ?: "")
        } When {
            post(CreateAppointmentPage.path)
        }
    }

}
