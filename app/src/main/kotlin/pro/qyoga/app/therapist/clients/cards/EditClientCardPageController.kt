package pro.qyoga.app.therapist.clients.cards

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.value
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.ClientPageModel
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.cards.model.Client
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

        return ClientPageModel(
            client,
            ClientPageTab.CARD,
            EditClientCardPageModel(formAction(client.id))
        )
    }

    @PostMapping(EDIT_CLIENT_CARD_PAGE_PATH)
    fun editClientCard(
        clientCardDto: ClientCardDto,
        @PathVariable clientId: UUID
    ): Any {
        val res = runCatching {
            clientsRepo.updateById(clientId) { client -> client.patchedBy(clientCardDto) }
        }

        return when (res.value()) {
            is Client -> hxRedirect("/therapist/clients", "HX-Trigger" to "formSaved")

            null -> notFound
            is DuplicatedPhoneException -> editClientFormWithValidationError(formAction(clientId), clientCardDto)
            else -> throw res.exceptionOrNull()!!
        }
    }

    companion object {

        const val EDIT_CLIENT_CARD_PAGE_PATH = "/therapist/clients/{clientId}/card"

    }

}

private fun formAction(clientId: UUID): String = "/therapist/clients/${clientId}/card"