package pro.qyoga.app.therapist.clients.list

import org.springframework.data.domain.Page
import org.springframework.web.servlet.ModelAndView
import pro.azhidkov.platform.spring.mvc.viewId
import pro.azhidkov.platform.ui.PeriodFormatter
import pro.qyoga.core.clients.cards.dtos.ClientSearchDto
import pro.qyoga.core.clients.cards.model.HasPersonName
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.l10n.russianDayOfMonthFormat
import java.time.Instant
import java.time.LocalDate
import java.time.Period
import java.util.*


data class ClientListItemView(
    val id: UUID,
    override val firstName: String,
    override val lastName: String,
    override val middleName: String?,
    val journalEntriesCount: Int,
    private val lastJournalEntryDate: LocalDate?,
    val therapistRef: TherapistRef,
    val createdAt: Instant
) : HasPersonName {

    fun lastJournalEntryDateLabel(now: LocalDate): String = when (lastJournalEntryDate) {
        null ->
            NO_LAST_JOURNAL_ENTRY_DATE

        in ((now - Period.ofMonths(1))..now) ->
            lastJournalEntryDate.format(russianDayOfMonthFormat)

        else ->
            "Более ${PeriodFormatter.formatPeriodInGenitiveCase(Period.between(lastJournalEntryDate, now))} назад"
    }

    companion object {
        const val NO_LAST_JOURNAL_ENTRY_DATE = "—"
    }

}

data class ClientsListPageModel(
    val clients: Page<ClientListItemView>,
    val today: LocalDate,
    val searchDto: ClientSearchDto,
    val fragment: String? = null
) : ModelAndView(
    viewId("therapist/clients/clients-list", fragment), mapOf(
        "clients" to clients,
        "today" to today,
        "searchDto" to searchDto,
        "pageNumbers" to 1..clients.totalPages
    )
)