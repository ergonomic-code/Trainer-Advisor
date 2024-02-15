package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.platform.seeOther
import pro.qyoga.app.publc.components.toComboBoxItem
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.*
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


@Controller
@RequestMapping("/therapist/appointments/{appointmentId}")
class EditAppointmentPageController(
    private val appointmentsRepo: AppointmentsRepo,
    private val timeZones: TimeZones,
    private val updateAppointment: UpdateAppointmentWorkflow
) {

    @GetMapping
    fun getEditAppointmentPage(@PathVariable appointmentId: Long): ModelAndView {
        val appointment = appointmentsRepo.findById(appointmentId, Appointment.Fetch.editableRefs)
            ?: return notFound

        return appointmentPageModelAndView(
            pageMode = EntityPageMode.EDIT,
            allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem)
        ) {
            "appointmentId" bindTo appointmentId
            "appointment" bindTo appointment.toEditRequest(timeZones::findById)
        }
    }

    @PutMapping
    fun editAppointment(
        @PathVariable appointmentId: Long,
        editAppointmentRequest: EditAppointmentRequest,
        @AuthenticationPrincipal therapist: QyogaUserDetails,
    ): ModelAndView {
        return try {
            updateAppointment(therapist.ref, AppointmentRef(appointmentId), editAppointmentRequest)
                ?: return notFound

            seeOther(SchedulePageController.PATH)
        } catch (ex: AppointmentsIntersectionException) {
            appointmentPageModelAndView(
                pageMode = EntityPageMode.EDIT,
                allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem)
            ) {
                "appointment" bindTo editAppointmentRequest
                "appointmentsIntersectionError" bindTo true
                "existingAppointment" bindTo ex.existingAppointment
            }
        }
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteAppointment(
        @PathVariable appointmentId: Long
    ) {
        appointmentsRepo.deleteById(appointmentId)
    }

}