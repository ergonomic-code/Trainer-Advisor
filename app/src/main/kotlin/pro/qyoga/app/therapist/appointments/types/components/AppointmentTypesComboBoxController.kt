package pro.qyoga.app.therapist.appointments.types.components

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.qyoga.app.platform.components.combobox.ComboBoxController
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.app.platform.components.combobox.ComboBoxModelAndView
import pro.qyoga.core.appointments.types.AppointmentTypesRepo
import pro.qyoga.core.appointments.types.findByNameContaining
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.therapists.ref


@Controller
@RequestMapping(AppointmentTypesComboBoxController.PATH)
class AppointmentTypesComboBoxController(
    private val appointmentTypesRepo: AppointmentTypesRepo
) : ComboBoxController<QyogaUserDetails> {

    @GetMapping
    override fun search(
        @RequestParam("appointmentTypeTitle") searchKey: String?,
        currentValue: String?,
        userDetails: QyogaUserDetails
    ): ComboBoxModelAndView {
        val searchResult = appointmentTypesRepo.findByNameContaining(
            userDetails.ref,
            searchKey ?: "",
            AppointmentTypesRepo.Page.topFiveByName
        )
            .map { ComboBoxItem(it.id, it.name) }
        return ComboBoxModelAndView(searchResult)
    }

    companion object {
        const val PATH: String = "/therapist/appointments/types/autocomplete-search"
    }

}
