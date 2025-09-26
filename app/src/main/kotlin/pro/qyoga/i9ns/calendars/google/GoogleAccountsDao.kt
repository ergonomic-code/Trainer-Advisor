package pro.qyoga.i9ns.calendars.google

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.findAllById
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.jdbc.taDataClassRowMapper
import pro.azhidkov.platform.spring.sdj.query.query
import pro.qyoga.core.users.therapists.TherapistRef


private val googleAccountRowMapper = taDataClassRowMapper<GoogleAccount>()

@Repository
class GoogleAccountsDao(
    private val jdbcAggregateTemplate: JdbcAggregateTemplate,
    private val jdbcClient: JdbcClient
) {

    fun addGoogleAccount(googleAccount: GoogleAccount) {
        jdbcAggregateTemplate.insert(googleAccount)
    }

    fun findGoogleAccounts(therapist: TherapistRef): List<GoogleAccount> {
        val query = query {
            GoogleAccount::ownerRef isEqual therapist
        }
        return jdbcAggregateTemplate.findAll(query, GoogleAccount::class.java)
    }

    fun findGoogleAccounts(accountIds: List<GoogleAccountRef>): List<GoogleAccount> {
        return jdbcAggregateTemplate.findAllById(accountIds.map { it.id })
    }

    fun findGoogleAccount(therapistRef: TherapistRef, calendarId: String): List<GoogleAccount> {
        val query = """
            SELECT *
            FROM therapist_google_accounts
                JOIN therapist_google_calendar_settings 
                    ON therapist_google_accounts.id = therapist_google_calendar_settings.google_account_ref
            WHERE therapist_google_accounts.owner_ref = :therapistRef
                AND calendar_id = :calendarId
        """.trimIndent()

        return jdbcClient.sql(query)
            .param("therapistRef", therapistRef.id)
            .param("calendarId", calendarId)
            .query(googleAccountRowMapper)
            .list()
    }

}
