package pro.qyoga.tests.clients.api

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.springframework.http.HttpCookie
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.client.RestTestClient
import org.springframework.util.MultiValueMap
import pro.azhidkov.platform.java.time.toLocalTimeString
import pro.qyoga.app.therapist.appointments.core.edit.CreateAppointmentPageController
import pro.qyoga.app.therapist.appointments.core.edit.EditAppointmentPageController
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageModel
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.AppointmentRef
import pro.qyoga.tests.infra.web.pageRestTestClient
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentPage
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentPage
import pro.qyoga.tests.platform.spring.rest_test_client.formData
import pro.qyoga.tests.platform.spring.rest_test_client.getBodyAsString
import java.net.URI
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class TherapistAppointmentsApi(
    override val authCookie: HttpCookie,
    private val restTestClient: RestTestClient = pageRestTestClient
) : AuthorizedApi {

    fun getScheduleForDay(date: LocalDate? = null, appointmentToFocus: AppointmentRef? = null): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(SchedulePageController.PATH).apply {
                    if (date != null) {
                        queryParam(SchedulePageController.DATE, date.toString())
                    }
                    if (appointmentToFocus != null) {
                        queryParam(SchedulePageModel.FOCUSED_APPOINTMENT, appointmentToFocus.id.toString())
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

    fun getCreateAppointmentPage(
        dateTime: LocalDateTime? = null,
        sourceItem: URI? = null
    ): Document {
        return restTestClient.get()
            .uri { builder ->
                builder.path(CreateAppointmentPage.path).apply {
                    if (dateTime != null) {
                        queryParam(CreateAppointmentPageController.DATE_TIME, dateTime.toString())
                    }
                    if (sourceItem != null) {
                        queryParam(CreateAppointmentPageController.SOURCE_ITEM, sourceItem.toASCIIString())
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

    fun getEditAppointmentPage(appointmentRef: AppointmentRef, expectedStatus: HttpStatus = HttpStatus.OK): Document {
        return restTestClient.get()
            .uri(EditAppointmentPageController.PATH, appointmentRef.id)
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(expectedStatus.value())
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun createAppointment(appointment: EditAppointmentRequest): RestTestClient.ResponseSpec {
        return restTestClient.post()
            .uri(CreateAppointmentPage.path)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(appointmentForm(appointment))
            .authorized()
            .exchange()
    }

    fun createAppointmentForError(appointment: EditAppointmentRequest): Document {
        return restTestClient.post()
            .uri(CreateAppointmentPage.path)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(appointmentForm(appointment))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectHeader().doesNotExist("HX-Redirect")
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    fun editAppointment(appointmentRef: AppointmentRef, appointment: EditAppointmentRequest): RestTestClient.ResponseSpec {
        return restTestClient.put()
            .uri(EditAppointmentPage.path, appointmentRef.id)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(appointmentForm(appointment))
            .authorized()
            .exchange()
    }

    fun editAppointmentForError(appointmentRef: AppointmentRef, appointment: EditAppointmentRequest): Document {
        return restTestClient.put()
            .uri(EditAppointmentPage.path, appointmentRef.id)
            .accept(MediaType.TEXT_HTML)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body(appointmentForm(appointment))
            .authorized()
            .exchange()
            .expectStatus().isEqualTo(HttpStatus.OK)
            .expectHeader().doesNotExist("HX-Redirect")
            .getBodyAsString()
            .let { Jsoup.parse(it) }
    }

    private fun appointmentForm(appointment: EditAppointmentRequest): MultiValueMap<String, String> {
        val form = CreateAppointmentPage.editAppointmentForm
        return formData(
            form.clientInput.name to appointment.client.id,
            form.clientInput.titleInput.name to appointment.clientTitle,
            form.typeInput.name to (appointment.appointmentType?.id ?: ""),
            form.typeInput.titleInputId to appointment.appointmentTypeTitle,
            form.therapeuticTaskInput.name to (appointment.therapeuticTask?.id ?: ""),
            form.therapeuticTaskInput.titleInput.name to (appointment.therapeuticTask?.id ?: ""),
            form.dateTime.name to appointment.dateTime.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME),
            form.timeZone.name to appointment.timeZone.id,
            form.timeZone.titleInput.name to appointment.timeZoneTitle,
            form.duration.name to appointment.duration.toLocalTimeString(),
            form.place.name to (appointment.place ?: ""),
            form.cost.name to (appointment.cost ?: ""),
            form.statusPending.name to appointment.appointmentStatus.name,
            form.comment.name to (appointment.comment ?: "")
        ).apply {
            if (appointment.payed == true) {
                add(form.payed.name, "true")
            }
        }
    }

    fun delete(appointmentRef: AppointmentRef, returnTo: LocalDate): RestTestClient.ResponseSpec {
        return restTestClient.delete()
            .uri { builder ->
                builder.path(EditAppointmentPageController.PATH).apply {
                    queryParam(EditAppointmentPageController.RETURN_TO, returnTo.toString())
                }.build(mapOf("appointmentId" to appointmentRef.id))
            }
            .accept(MediaType.TEXT_HTML)
            .authorized()
            .exchange()
    }

}
