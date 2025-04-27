package pro.qyoga.app.therapist.clients.cards

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.mapNull
import pro.azhidkov.platform.kotlin.mapSuccessOrNull
import pro.azhidkov.platform.kotlin.recoverFailure
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.ClientPageFragmentModel
import pro.qyoga.app.therapist.clients.ClientPageModel
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.cards.patchedBy
import java.util.*

data class EditClientCardPageModel(
    private val formAction: String
) : ClientPageFragmentModel,
    ModelAndView(
        viewId("/therapist/clients/client-edit"),
        mapOf(
            "formAction" to formAction
        )
    ) {

    override val model: ModelMap = super<ModelAndView>.modelMap

}

@Controller
class EditClientCardPageController(
    private val clientsRepo: ClientsRepo
) {

    @GetMapping(EDIT_CLIENT_CARD_PAGE_PATH)
    fun getEditClientCardPage(
        @PathVariable clientId: UUID
    ): ModelAndView {
        val client = clientsRepo.findByIdOrNull(clientId)
            ?: return notFound

        return ClientPageModel(
            client,
            ClientPageTab.CARD,
            EditClientCardPageModel(
                "/therapist/clients/${client.id}/card"
            )
        )
    }

    @PostMapping(EDIT_CLIENT_CARD_PAGE_PATH)
    fun editClientCard(
        clientCardDto: ClientCardDto,
        @PathVariable clientId: UUID
    ): ModelAndView {
        val res = runCatching {
            clientsRepo.updateById(clientId) { client -> client.patchedBy(clientCardDto) }
        }

        val modelAndView = res
            .mapSuccessOrNull {
                ModelAndView("redirect:/therapist/clients")
            }
            .mapNull {
                notFound
            }
            .recoverFailure { _: DuplicatedPhoneException ->
                editClientFormWithValidationError(clientCardDto)
            }
            .getOrThrow()

        return modelAndView
    }

    companion object {

        const val EDIT_CLIENT_CARD_PAGE_PATH = "/therapist/clients/{clientId}/card"

    }

}