package pro.qyoga.app.therapist.clients

import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.modelAndView
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.toDto


enum class ClientPageTab {
    JOURNAL,
    CARD,
    FILES,
    ADD_JOURNAL_ENTRY
}

fun clientPageModel(
    client: Client,
    activeTab: ClientPageTab,
    fragmentModel: Map<String, Any?>
): ModelAndView {
    val modelAndView = modelAndView(
        "therapist/clients/client-edit", mapOf(
            "client" to client.toDto(),
            "activeTab" to activeTab,
        ) + fragmentModel
    )
    return modelAndView
}