package pro.qyoga.app.therapist.clients

import org.springframework.web.servlet.ModelAndView
import pro.qyoga.core.clients.cards.api.Client
import pro.qyoga.platform.spring.mvc.ModelAndViewBuilder
import pro.qyoga.platform.spring.mvc.modelAndView


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
        "client" bindTo client
        "activeTab" bindTo activeTab
        fragmentModel()
    }
    return modelAndView
}