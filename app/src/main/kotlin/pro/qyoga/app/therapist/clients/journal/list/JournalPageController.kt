package pro.qyoga.app.therapist.clients.journal.list

import org.springframework.data.domain.Page
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.therapist.clients.ClientPageFragmentModel
import pro.qyoga.app.therapist.clients.ClientPageModel
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.core.clients.journals.dtos.JournalPageRq.Companion.firstPage
import pro.qyoga.core.clients.journals.model.JournalEntry
import java.util.*


class JournalPageFragmentModel(
    val page: Page<JournalEntry>
) :
    ClientPageFragmentModel,
    ModelAndView(
        viewId("therapist/clients/client-journal-fragment"), mapOf(
            "journal" to page
        )
    ) {

    override val model: ModelMap = super<ModelAndView>.modelMap

}

@Controller
class JournalPageController(
    private val getJournalPage: GetJournalPageOp
) {

    @GetMapping(JOURNAL_PAGE_PATH)
    fun handleGetJournalFragment(
        @PathVariable clientId: UUID
    ): ClientPageModel<JournalPageFragmentModel> {
        val firstPageRq = firstPage(clientId, fetch = listOf(JournalEntry::therapeuticTask))
        val (client, journalPage) = getJournalPage(firstPageRq)
        return ClientPageModel(
            client,
            ClientPageTab.JOURNAL,
            JournalPageFragmentModel(journalPage)
        )
    }

    companion object {

        const val JOURNAL_PAGE_PATH = "/therapist/clients/{clientId}/journal"

    }

}