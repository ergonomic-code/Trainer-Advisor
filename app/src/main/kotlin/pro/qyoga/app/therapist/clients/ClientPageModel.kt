package pro.qyoga.app.therapist.clients

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.ModelBuilder
import pro.azhidkov.platform.spring.mvc.modelAndView
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
    fragmentModel: ModelBuilder.() -> Unit
): ModelAndView {
    val modelAndView = modelAndView("therapist/clients/client-edit") {
        "client" bindTo client.toDto()
        "activeTab" bindTo activeTab
        fragmentModel()
    }
    return modelAndView
}