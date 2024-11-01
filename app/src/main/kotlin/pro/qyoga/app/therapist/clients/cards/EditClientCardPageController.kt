package pro.qyoga.app.therapist.clients.cards

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
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


@Controller
@RequestMapping("/therapist/clients/{id}/card")
class EditClientCardPageController(
    private val clientsRepo: ClientsRepo
) {

    @GetMapping
    fun getEditClientCardPage(
        @PathVariable id: Long,
        model: Model
    ): ModelAndView {
        val client = clientsRepo.findByIdOrNull(id)
            ?: return notFound

        return clientPageModel(client, ClientPageTab.CARD) {
            "formAction" bindTo "/therapist/clients/${client.id}/card"
        }
    }

    @PostMapping
    fun editClientCard(
        clientCardDto: ClientCardDto,
        @PathVariable id: Long
    ): ModelAndView {
        val res = runCatching {
            clientsRepo.updateById(id) { client -> client.patchedBy(clientCardDto) }
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

}