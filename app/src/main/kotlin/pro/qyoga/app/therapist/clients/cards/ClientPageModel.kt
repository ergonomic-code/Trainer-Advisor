package pro.qyoga.app.therapist.clients.cards

import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.clients.cards.dtos.ClientCardDto


fun editClientFormWithValidationError(clientCardDto: ClientCardDto) =
    modelAndView("therapist/clients/client-create") {
        "client" bindTo clientCardDto
        "duplicatedPhone" bindTo true
    }
