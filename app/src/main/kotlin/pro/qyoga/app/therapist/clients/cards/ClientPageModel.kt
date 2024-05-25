package pro.qyoga.app.therapist.clients.cards

import pro.azhidkov.platform.spring.mvc.modelAndView


fun editClientFormWithValidationError(editClientCardForm: EditClientCardForm) =
    modelAndView("therapist/clients/client-create") {
        "clientForm" bindTo editClientCardForm
        "duplicatedPhone" bindTo true
    }
