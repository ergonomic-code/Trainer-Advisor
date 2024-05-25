package pro.qyoga.app.therapist.clients

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.ModelAndViewBuilder
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.app.therapist.clients.cards.EditClientCardForm
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.toDto


enum class ClientPageTab {
    JOURNAL,
    CARD,
    FILES
}

fun clientPageModel(
    client: Client,
    activeTab: ClientPageTab,
    fragmentModel: ModelAndViewBuilder.() -> Unit
): ModelAndView {
    val modelAndView = modelAndView("therapist/clients/client-edit") {
        "clientForm" bindTo EditClientCardForm(client.toDto())
        "activeTab" bindTo activeTab
        fragmentModel()
    }
    return modelAndView
}