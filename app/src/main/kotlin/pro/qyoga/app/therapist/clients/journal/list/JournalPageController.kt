package pro.qyoga.app.therapist.clients.journal.list

import org.springframework.data.domain.Page
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.servlet.ModelAndView
import pro.qyoga.app.platform.notFound
import pro.qyoga.app.therapist.clients.ClientPageTab
import pro.qyoga.app.therapist.clients.clientPageModel
import pro.qyoga.core.clients.journals.dtos.JournalPageRequest
import pro.qyoga.core.clients.journals.model.JournalEntry


private const val JOURNAL = "journal"

@Controller
@RequestMapping("/therapist/clients")
class JournalPageController(
    private val getJournalPageWorkflow: GetJournalPageWorkflow
) {

    @GetMapping("/{id}/journal")
    fun getJournalPage(
        @PathVariable id: Long
    ): ModelAndView {
        val firstPage = JournalPageRequest.firstPage(id, fetch = listOf(JournalEntry::therapeuticTask))
        return when (val result = getJournalPageWorkflow(firstPage)) {
            is GetJournalPageResult.ClientNotFound ->
                notFound

            is GetJournalPageResult.Success -> {
                clientPageModel(result.client, ClientPageTab.JOURNAL) {
                    JOURNAL bindTo result.page
                }
            }
        }
    }

    companion object {

        @Suppress("UNCHECKED_CAST")
        fun getJournal(model: Map<String, Any>): Page<JournalEntry> = model[JOURNAL] as Page<JournalEntry>

    }

}