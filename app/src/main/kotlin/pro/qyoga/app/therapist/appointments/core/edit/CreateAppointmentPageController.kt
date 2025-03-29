package pro.qyoga.app.therapist.appointments.core.edit

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.azhidkov.timezones.LocalizedTimeZone
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.platform.EntityPageMode
import pro.qyoga.app.platform.seeOther
import pro.qyoga.app.publc.components.toComboBoxItem
import pro.qyoga.app.therapist.appointments.core.edit.errors.AppointmentsIntersectionException
import pro.qyoga.app.therapist.appointments.core.edit.ops.CreateAppointmentOp
import pro.qyoga.app.therapist.appointments.core.edit.ops.GetAppointmentPrefillDataOp
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.edit.view_model.appointmentPageModelAndView
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController.Companion.calendarForDayWithFocus
import pro.qyoga.core.appointments.core.commands.EditAppointmentRequest
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref
import java.time.LocalDateTime

@Controller
@RequestMapping(CreateAppointmentPageController.PATH)
class CreateAppointmentPageController(
    private val getAppointmentPrefillData: GetAppointmentPrefillDataOp,
    private val createAppointment: CreateAppointmentOp,
    private val timeZones: TimeZones
) {

    @GetMapping
    fun getAppointmentPage(
        @RequestParam(DATE_TIME) dateTime: LocalDateTime?,
        @RequestParam(SOURCE_ITEM_TYPE) sourceItemType: String?,
        @RequestParam(SOURCE_ITEM_ID) sourceItemId: String?,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): ModelAndView {
        val sourceItem = when {
            sourceItemType != null && sourceItemId != null -> SourceItem(sourceItemType, sourceItemId)
            else -> null
        }
        val prefillData = getAppointmentPrefillData(therapist.ref, sourceItem, dateTime)
        return appointmentPageModelAndView(
            pageMode = EntityPageMode.CREATE,
            allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem),
            additionalModel = mapOf(
                "appointment" to prefillData
            )
        )
    }

    @PostMapping
    fun createAppointment(
        editAppointmentRequest: EditAppointmentRequest,
        @AuthenticationPrincipal therapist: QyogaUserDetails
    ): Any {
        return try {
            val appointment = createAppointment(therapist.ref, editAppointmentRequest)
            seeOther(calendarForDayWithFocus(editAppointmentRequest.dateTime.toLocalDate(), appointment.ref()))
        } catch (ex: AppointmentsIntersectionException) {
            appointmentPageModelAndView(
                pageMode = EntityPageMode.CREATE,
                allAvailableTimeZones = timeZones.allTimeZones.map(LocalizedTimeZone::toComboBoxItem),
                additionalModel = mapOf(
                    "appointment" to editAppointmentRequest,
                    "appointmentsIntersectionError" to true,
                    "existingAppointment" to ex.existingAppointment
                )
            )
        }
    }

    companion object {
        const val PATH = "/therapist/appointments/new"
        const val DATE_TIME = "dateTime"
        const val SOURCE_ITEM_TYPE = "sourceItemType"
        const val SOURCE_ITEM_ID = "sourceItemId"
        const val ADD_TO_DATE_TIME_PATH = "/therapist/appointments/new?$DATE_TIME={$DATE_TIME}"
    }

}