package pro.qyoga.app.therapist.clients

import org.springframework.ui.ModelMap
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.core.clients.cards.model.Client
import pro.qyoga.core.clients.cards.toDto


enum class ClientPageTab {
    JOURNAL,
    CARD,
    FILES,
    ADD_JOURNAL_ENTRY
}

interface ClientPageFragmentModel {
    val model: ModelMap
}

data class ClientPageModel<T : ClientPageFragmentModel>(
    private val client: Client,
    private val activeTab: ClientPageTab,
    val fragmentModel: T
) : ModelAndView(
    viewId("therapist/clients/client-edit"), mapOf(
        "client" to client.toDto(),
        "activeTab" to activeTab,
    ) + fragmentModel.model
)
