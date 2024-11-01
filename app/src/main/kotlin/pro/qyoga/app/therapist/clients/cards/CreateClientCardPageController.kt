package pro.qyoga.app.therapist.clients.cards

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.mapSuccess
import pro.azhidkov.platform.kotlin.recoverFailure
import pro.qyoga.core.clients.cards.Client
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails


@Controller
@RequestMapping("/therapist/clients")
class CreateClientCardPageController(
    private val clientsRepo: ClientsRepo
) {

    @GetMapping("/create")
    fun getCreateClientPage(model: Model): String {
        model.addAttribute("formAction", "/therapist/clients/create")
        return "therapist/clients/client-create"
    }

    @PostMapping("/create")
    fun createClient(
        clientCardDto: ClientCardDto,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ModelAndView {
        val res = runCatching {
            clientsRepo.save(Client(principal.id, clientCardDto))
        }

        val modelAndView = res
            .mapSuccess {
                ModelAndView("redirect:/therapist/clients")
            }
            .recoverFailure { _: DuplicatedPhoneException ->
                editClientFormWithValidationError(clientCardDto)
            }
            .getOrThrow()

        return modelAndView
    }

}