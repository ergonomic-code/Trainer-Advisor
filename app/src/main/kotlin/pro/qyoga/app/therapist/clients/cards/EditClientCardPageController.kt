package pro.qyoga.app.therapist.clients.cards

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.mapNull
import pro.azhidkov.platform.kotlin.mapSuccessOrNull
import pro.azhidkov.platform.kotlin.recoverFailure
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.app.therapist.clients.clientPageModel
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.cards.patchedBy
import java.util.*


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

        return clientPageModel(client, ClientPageTab.CARD) {
            "formAction" bindTo "/therapist/clients/${client.id}/card"
        }
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