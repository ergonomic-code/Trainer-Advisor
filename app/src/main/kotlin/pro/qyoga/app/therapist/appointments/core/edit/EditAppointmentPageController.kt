package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.http.hxClientSideRedirect
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.platform.seeOther
import pro.qyoga.app.publc.components.toComboBoxItem
import pro.qyoga.app.therapist.appointments.core.edit.errors.AppointmentsIntersectionException
import pro.qyoga.app.therapist.appointments.core.edit.ops.UpdateAppointmentOp
import pro.qyoga.app.therapist.appointments.core.edit.view_model.appointmentPageModelAndView
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.core.model.AppointmentRef
import pro.qyoga.core.appointments.core.toEditRequest
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import java.time.LocalDate
import java.util.*


@Controller
@RequestMapping(EditAppointmentPageController.PATH)
class EditAppointmentPageController(
    private val appointmentsRepo: AppointmentsRepo,
    private val timeZones: TimeZones,
    private val updateAppointment: UpdateAppointmentOp
) {

    @GetMapping
    fun getEditAppointmentPage(@PathVariable appointmentId: UUID): ModelAndView {
        val appointment = appointmentsRepo.findById(appointmentId, Appointment.Fetch.editableRefs)
            ?: return notFound

        return appointmentPageModelAndView(
            pageMode = EntityPageMode.EDIT,
            allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem),
            additionalModel = mapOf(
                "appointmentId" to appointmentId,
                "appointment" to appointment.toEditRequest(timeZones::findById),
            )
        )
    }

    @PutMapping
    fun editAppointment(
        @PathVariable appointmentId: UUID,
        editAppointmentRequest: EditAppointmentRequest,
        @AuthenticationPrincipal therapist: QyogaUserDetails,
    ): ModelAndView {
        return try {
            updateAppointment(therapist.ref, AppointmentRef(appointmentId), editAppointmentRequest)
                ?: return notFound

            seeOther(
                SchedulePageController.calendarForDayWithFocus(
                    editAppointmentRequest.dateTime.toLocalDate(),
                    AppointmentRef(appointmentId)
                )
            )
        } catch (ex: AppointmentsIntersectionException) {
            appointmentPageModelAndView(
                pageMode = EntityPageMode.EDIT,
                allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem),
                additionalModel = mapOf(
                    "appointment" to editAppointmentRequest,
                    "appointmentsIntersectionError" to true,
                    "existingAppointment" to ex.existingAppointment
                )
            )
        }
    }

    @DeleteMapping
    fun deleteAppointment(
        @PathVariable appointmentId: UUID,
        @RequestParam(RETURN_TO) returnTo: LocalDate
    ): ResponseEntity<Unit> {
        appointmentsRepo.deleteById(appointmentId)
        return hxClientSideRedirect(SchedulePageController.calendarForDateUrl(returnTo))
    }

    companion object {
        const val PATH = "/therapist/appointments/{appointmentId}"
        const val RETURN_TO = "returnTo"
        const val DELETE_PATH = "$PATH?$RETURN_TO={$RETURN_TO}"

        fun editUri(appointmentId: UUID) = PATH.replace("{appointmentId}", appointmentId.toString())
    }

}