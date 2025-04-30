package pro.qyoga.app.therapist.clients.journal.list

import org.springframework.data.domain.Slice
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.qyoga.app.therapist.clients.ClientPageFragmentModel
import pro.qyoga.app.therapist.clients.ClientPageModel
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.core.clients.journals.dtos.JournalPageRq
import pro.qyoga.core.clients.journals.dtos.JournalPageRq.Companion.firstPage
import pro.qyoga.core.clients.journals.model.JournalEntry
import java.time.LocalDate
import java.util.*


class JournalPageFragmentModel(
    val page: Slice<JournalEntry>,
) :
    ClientPageFragmentModel,
    ModelAndView(
        viewId("therapist/clients/client-journal-fragment", "journal-entries"), mapOf(
            "journal" to page,
            "lastEntryDate" to page.lastOrNull()?.date
        )
    ) {

    override val model: ModelMap = super<ModelAndView>.modelMap

}

@Controller
class JournalPageController(
    private val getJournalPage: GetJournalPageOp
) {

    @GetMapping(JOURNAL_PAGE_PAGE_PATH)
    fun handleGetJournalEntriesPageFragment(
        @PathVariable clientId: UUID,
        @RequestParam after: LocalDate
    ): ModelAndView {
        val firstPageRq = JournalPageRq.page(clientId, after, fetch = listOf(JournalEntry::therapeuticTask))
        val (client, journalPage) = getJournalPage(firstPageRq)
        return JournalPageFragmentModel(journalPage)
    }

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
        const val JOURNAL_PAGE_PAGE_PATH = "/therapist/clients/{clientId}/journal/page"

        fun nextPageUrl(lastEntry: JournalEntry): String = JOURNAL_PAGE_PAGE_PATH.replace(
            "\\{clientId}".toRegex(),
            lastEntry.clientRef.id.toString()
        ) + "?after=${lastEntry.date}"

    }

}