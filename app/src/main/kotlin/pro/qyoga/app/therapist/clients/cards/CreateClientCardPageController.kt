package pro.qyoga.app.therapist.clients.cards

import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.kotlin.mapFailure
import pro.azhidkov.platform.kotlin.mapSuccess
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.clients.cards.Client
import pro.qyoga.core.clients.cards.ClientsRepo
import pro.qyoga.core.clients.cards.dtos.ClientCardDto
import pro.qyoga.core.clients.cards.errors.DuplicatedPhoneException
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptorsRepo
import pro.qyoga.core.clients.therapeutic_data.descriptors.findByTherapistId
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails


@Controller
@RequestMapping("/therapist/clients")
class CreateClientCardPageController(
    private val clientsRepo: ClientsRepo,
    private val therapeuticDataDescriptorsRepo: TherapeuticDataDescriptorsRepo
) {

    @GetMapping("/create")
    fun getCreateClientPage(
        model: Model,
        @AuthenticationPrincipal principal: QyogaUserDetails,
    ): ModelAndView {
        val therapeuticDataDescriptor = therapeuticDataDescriptorsRepo.findByTherapistId(principal.id)
        return modelAndView("therapist/clients/client-create") {
            "formAction" bindTo "/therapist/clients/create"
            "therapeuticDataDescriptor" bindTo therapeuticDataDescriptor
        }
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
            .mapFailure { _: DuplicatedPhoneException ->
                editClientFormWithValidationError(clientCardDto)
            }
            .getOrThrow()

        return modelAndView
    }

}