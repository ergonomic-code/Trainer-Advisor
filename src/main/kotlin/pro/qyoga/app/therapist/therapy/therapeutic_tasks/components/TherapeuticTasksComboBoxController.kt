package pro.qyoga.app.therapist.therapy.therapeutic_tasks.components

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.qyoga.app.platform.components.combobox.ComboBoxController
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.app.platform.components.combobox.ComboBoxModelAndView
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo
import pro.qyoga.core.therapy.therapeutic_tasks.TherapeuticTasksRepo.Page.topFiveByName
import pro.qyoga.core.therapy.therapeutic_tasks.findByNameContaining
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails


@Controller
@RequestMapping(TherapeuticTasksComboBoxController.PATH)
class TherapeuticTasksComboBoxController(
    private val therapeuticTasksRepo: TherapeuticTasksRepo
) : ComboBoxController<QyogaUserDetails> {

    @GetMapping
    override fun search(
        @RequestParam("therapeuticTaskTitle") searchKey: String?,
        @RequestParam currentValue: String?,
        @AuthenticationPrincipal userDetails: QyogaUserDetails
    ): ComboBoxModelAndView {
        val searchResult =
            therapeuticTasksRepo.findByNameContaining(userDetails.id, searchKey, topFiveByName)
                .map { ComboBoxItem(it.id, it.name) }

        return ComboBoxModelAndView((searchResult))
    }

    companion object {
        const val PATH = "/therapist/therapeutic-tasks/autocomplete-search-combo-box"
    }

}