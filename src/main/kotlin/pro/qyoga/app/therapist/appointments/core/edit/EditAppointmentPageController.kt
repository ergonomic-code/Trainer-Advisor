package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.common.EntityPageMode
import pro.qyoga.app.common.notFound
import pro.qyoga.app.common.seeOther
import pro.qyoga.app.components.toComboBoxItem
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.AppointmentsRepo
import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.core.model.Appointment
import pro.qyoga.core.appointments.core.model.AppointmentRef
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


@Controller
@RequestMapping("/therapist/appointments/{appointmentId}")
class EditAppointmentPageController(
    private val appointmentsRepo: AppointmentsRepo,
    private val timeZones: TimeZones,
    private val updateAppointmentWorkflow: UpdateAppointmentWorkflow
) {

    @GetMapping
    fun getEditAppointmentPage(@PathVariable appointmentId: Long): ModelAndView {
        val appointment = appointmentsRepo.findById(appointmentId, Appointment.Fetch.editableRefs)
            ?: return notFound

        return appointmentPageModelAndView(
            pageMode = EntityPageMode.EDIT,
            allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem)
        ) {
            "appointment" bindTo appointment
            "currentTimeZone" bindTo timeZones.findById(appointment.timeZone)
        }
    }

    @PutMapping
    fun editAppointment(
        @PathVariable appointmentId: Long,
        editAppointmentRequest: EditAppointmentRequest,
        @AuthenticationPrincipal therapist: QyogaUserDetails,
    ): ModelAndView {
        updateAppointmentWorkflow(therapist.ref, AppointmentRef(appointmentId), editAppointmentRequest)
            ?: return notFound

        return seeOther(SchedulePageController.PATH)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteAppointment(
        @PathVariable appointmentId: Long
    ) {
        appointmentsRepo.deleteById(appointmentId)
    }

}