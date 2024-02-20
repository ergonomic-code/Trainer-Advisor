package pro.qyoga.app.therapist.clients.components

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import pro.qyoga.app.platform.components.combobox.ComboBoxController
import pro.qyoga.app.platform.components.combobox.ComboBoxItem
import pro.qyoga.app.platform.components.combobox.ComboBoxModelAndView
import pro.qyoga.core.clients.cards.internal.ClientsRepo
import pro.qyoga.core.clients.cards.internal.findPageBy
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails


@Controller
@RequestMapping(ClientsComboBoxController.PATH)
class ClientsComboBoxController(
    private val clientsRepo: ClientsRepo
) : ComboBoxController<QyogaUserDetails> {

    @GetMapping
    override fun search(
        @RequestParam("clientTitle") searchKey: String?,
        @RequestParam currentValue: String?,
        @AuthenticationPrincipal userDetails: QyogaUserDetails
    ): ComboBoxModelAndView {
        val searchResult =
            clientsRepo.findPageBy(userDetails.id, searchKey ?: "", page = ClientsRepo.Page.topFiveByLastName)
                .map { ComboBoxItem(it.id, it.fullName(), it.phoneNumber) }

        return ComboBoxModelAndView(searchResult)
    }

    companion object {
        const val PATH = "/therapist/clients/autocomplete-search"
    }

}
