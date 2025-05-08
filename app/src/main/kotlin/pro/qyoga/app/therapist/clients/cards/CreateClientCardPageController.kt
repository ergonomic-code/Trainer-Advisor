package pro.qyoga.app.therapist.clients.cards

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import pro.azhidkov.platform.kotlin.value
import pro.azhidkov.platform.spring.http.hxRedirect
import pro.qyoga.app.platform.notFound
import pro.qyoga.core.clients.cards.Client
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails


@Controller
@RequestMapping("/therapist/clients")
class CreateClientCardPageController(
    private val clientsRepo: ClientsRepo
) {

    @GetMapping("/create")
    fun getCreateClientPage(model: Model): String {
        model.addAttribute("formAction", FORM_ACTION)
        return "therapist/clients/client-create"
    }

    @PostMapping("/create")
    fun createClient(
        clientCardDto: ClientCardDto,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): Any {
        val res = runCatching {
            clientsRepo.save(Client(principal.id, clientCardDto))
        }

        return when (res.value()) {
            is Client -> hxRedirect("/therapist/clients", "HX-Trigger" to "formSaved")

            null -> notFound
            is DuplicatedPhoneException -> editClientFormWithValidationError(FORM_ACTION, clientCardDto)
            else -> throw res.exceptionOrNull()!!
        }
    }

    companion object {
        private const val FORM_ACTION = "/therapist/clients/create"
    }

}