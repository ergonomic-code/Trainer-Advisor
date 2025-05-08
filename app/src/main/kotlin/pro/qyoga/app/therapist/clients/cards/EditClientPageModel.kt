package pro.qyoga.app.therapist.clients.cards

import org.springframework.ui.ModelMap
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.therapist.clients.ClientPageFragmentModel
import pro.qyoga.core.clients.cards.dtos.ClientCardDto

data class EditClientCardPageModel(
    private val formAction: String,
    private val duplicatedPhone: Boolean = false
) : ClientPageFragmentModel,
    ModelAndView(
        viewId("/therapist/clients/client-edit"),
        mapOf(
            "formAction" to formAction,
            "duplicatedPhone" to duplicatedPhone
        )
    ) {

    override val model: ModelMap = super<ModelAndView>.modelMap

}

fun editClientFormWithValidationError(formAction: String, clientCardDto: ClientCardDto) =
    modelAndView(
        "therapist/clients/client-create", mapOf(
            "formAction" to formAction,
            "client" to clientCardDto,
            "duplicatedPhone" to true
        )
    )
