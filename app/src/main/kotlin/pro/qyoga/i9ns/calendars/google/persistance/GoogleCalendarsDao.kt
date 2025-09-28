package pro.qyoga.i9ns.calendars.google.persistance

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.jdbc.taDataClassRowMapper
import pro.azhidkov.platform.spring.sdj.ergo.hydration.FetchSpec
import pro.azhidkov.platform.spring.sdj.ergo.hydration.hydrate
import pro.azhidkov.platform.uuid.UUIDv7
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.i9ns.calendars.google.model.GoogleAccountRef
import pro.qyoga.i9ns.calendars.google.model.GoogleCalendarSettings
import kotlin.reflect.KProperty1

typealias GoogleCalendarSettingsPatch = Map<String, Any>

@Repository
class GoogleCalendarsDao(
    private val jdbcClient: JdbcClient,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
) {

    private val googleCalendarSettingsRowMapper = taDataClassRowMapper<GoogleCalendarSettings>()

    fun patchCalendarSettings(
        therapist: TherapistRef,
        googleAccount: GoogleAccountRef,
        calendarId: String,
        settingsPatch: GoogleCalendarSettingsPatch
    ) {
        val query = """
            INSERT INTO therapist_google_calendar_settings (id, owner_ref, google_account_ref, calendar_id, should_be_shown)
            VALUES (:id, :ownerRef, :googleAccountRef::uuid, :calendarId, :shouldBeShown)
            ON CONFLICT (owner_ref, google_account_ref, calendar_id) DO UPDATE SET should_be_shown = EXCLUDED.should_be_shown
        """.trimIndent()

        jdbcClient.sql(query)
            .param("id", UUIDv7.randomUUID())
            .param("ownerRef", therapist.id)
            .param("googleAccountRef", googleAccount.id)
            .param("calendarId", calendarId)
            .param("shouldBeShown", settingsPatch["shouldBeShown"] as Boolean)
            .update()
    }

    fun findCalendarsSettings(
        therapist: TherapistRef,
        shouldBeShown: Boolean? = null,
        fetch: Iterable<KProperty1<GoogleCalendarSettings, *>> = emptyList()
    ): List<GoogleCalendarSettings> {
        val query = """
            SELECT * 
            FROM therapist_google_calendar_settings 
            WHERE owner_ref = :ownerRef
                ${shouldBeShown?.let { "AND should_be_shown = :shouldBeShown" } ?: ""}
        """

        @Suppress("SqlSourceToSinkFlow")
        var calendars = jdbcClient.sql(query)
            .param("ownerRef", therapist.id)
            .param("shouldBeShown", shouldBeShown)
            .query(googleCalendarSettingsRowMapper)
            .list()

        calendars = jdbcAggregateTemplate.hydrate(calendars, FetchSpec(fetch))
        return calendars
    }

}
