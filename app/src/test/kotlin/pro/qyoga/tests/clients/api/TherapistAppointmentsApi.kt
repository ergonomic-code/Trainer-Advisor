package pro.qyoga.tests.clients.api

import io.restassured.http.Cookie
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import io.restassured.response.Response
import io.restassured.specification.RequestSpecification
import org.hamcrest.CoreMatchers.nullValue
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.java.time.toLocalTimeString
import pro.qyoga.app.therapist.appointments.core.edit.CreateAppointmentPageController
import pro.qyoga.app.therapist.appointments.core.edit.EditAppointmentPageController
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.schedule.CalendarPageModel
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.AppointmentRef
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentPage
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentPage
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class TherapistAppointmentsApi(override val authCookie: Cookie) : AuthorizedApi {

    fun getScheduleForDay(date: LocalDate? = null, appointmentToFocus: AppointmentRef? = null): Document {
        return Given {
            authorized()
            if (date != null) {
                queryParam(SchedulePageController.DATE, date.toString())
            }
            if (appointmentToFocus != null) {
                queryParam(CalendarPageModel.FOCUSED_APPOINTMENT, appointmentToFocus.id.toString())
            }
            this
        } When {
            get(SchedulePageController.PATH)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getCreateAppointmentPage(
        dateTime: LocalDateTime? = null,
        sourceItem: SourceItem? = null
    ): Document {
        return Given {
            authorized()
            if (dateTime != null) {
                queryParam(CreateAppointmentPageController.DATE_TIME, dateTime.toString())
            }
            if (sourceItem != null) {
                queryParam(CreateAppointmentPageController.SOURCE_ITEM_TYPE, sourceItem.type)
                queryParam(CreateAppointmentPageController.SOURCE_ITEM_ID, sourceItem.id)
            }
            this
        } When {
            get(CreateAppointmentPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun getEditAppointmentPage(appointmentRef: AppointmentRef, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return Given {
            authorized()

            pathParam("appointmentId", appointmentRef.id)
        } When {
            get(EditAppointmentPageController.PATH)
        } Then {
            statusCode(expectedStatus.value())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun createAppointment(appointment: EditAppointmentRequest): Response {
        return Given {
            authorized()

            fillAppointmentForm(appointment)
        } When {
            post(CreateAppointmentPage.path)
        }
    }

    fun createAppointmentForError(appointment: EditAppointmentRequest): Document {
        return Given {
            authorized()
            fillAppointmentForm(appointment)
        } When {
            post(CreateAppointmentPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
            header("HX-Redirect", nullValue())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    fun editAppointment(appointmentRef: AppointmentRef, appointment: EditAppointmentRequest): Response {
        return Given {
            authorized()
            pathParam("appointmentId", appointmentRef.id)
            fillAppointmentForm(appointment)
        } When {
            put(EditAppointmentPage.path)
        }
    }

    fun editAppointmentForError(appointmentRef: AppointmentRef, appointment: EditAppointmentRequest): Document {
        return Given {
            authorized()
            pathParam("appointmentId", appointmentRef.id)
            fillAppointmentForm(appointment)
        } When {
            put(EditAppointmentPage.path)
        } Then {
            statusCode(HttpStatus.OK.value())
            header("HX-Redirect", nullValue())
        } Extract {
            Jsoup.parse(body().asString())
        }
    }

    private fun RequestSpecification.fillAppointmentForm(appointment: EditAppointmentRequest): RequestSpecification {
        formParam(CreateAppointmentPage.editAppointmentForm.clientInput.name, appointment.client.id)
        formParam(CreateAppointmentPage.editAppointmentForm.clientInput.titleInput.name, appointment.clientTitle)

        formParam(CreateAppointmentPage.editAppointmentForm.typeInput.name, appointment.appointmentType?.id ?: "")
        formParam(
            CreateAppointmentPage.editAppointmentForm.typeInput.titleInputId,
            appointment.appointmentTypeTitle
        )

        formParam(
            CreateAppointmentPage.editAppointmentForm.therapeuticTaskInput.name,
            appointment.therapeuticTask?.id ?: ""
        )
        formParam(
            CreateAppointmentPage.editAppointmentForm.therapeuticTaskInput.titleInput.name,
            appointment.therapeuticTask?.id ?: ""
        )

        formParam(
            CreateAppointmentPage.editAppointmentForm.dateTime.name,
            appointment.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        )
        formParam(
            CreateAppointmentPage.editAppointmentForm.timeZone.name,
            appointment.timeZone.id
        )
        formParam(
            CreateAppointmentPage.editAppointmentForm.timeZone.titleInput.name,
            appointment.timeZoneTitle
        )
        formParam(
            CreateAppointmentPage.editAppointmentForm.duration.name,
            appointment.duration.toLocalTimeString()
        )
        formParam(CreateAppointmentPage.editAppointmentForm.place.name, appointment.place ?: "")

        formParam(CreateAppointmentPage.editAppointmentForm.cost.name, appointment.cost ?: "")
        if (appointment.payed == true) {
            formParam(CreateAppointmentPage.editAppointmentForm.payed.name, "true")
        }
        formParam(CreateAppointmentPage.editAppointmentForm.statusPending.name, appointment.appointmentStatus.name)

        return formParam(CreateAppointmentPage.editAppointmentForm.comment.name, appointment.comment ?: "")
    }

    fun delete(appointmentRef: AppointmentRef, returnTo: LocalDate): Response {
        return Given {
            authorized()
            pathParam("appointmentId", appointmentRef.id)
            queryParam(EditAppointmentPageController.RETURN_TO, returnTo.toString())
        } When {
            delete(EditAppointmentPageController.PATH)
        }
    }

}
