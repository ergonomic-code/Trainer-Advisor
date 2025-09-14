package pro.qyoga.core.calendar.google

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.query.query
import pro.qyoga.core.users.therapists.TherapistRef


@Repository
class GoogleAccountsDao(
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
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

}