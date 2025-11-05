package pro.qyoga.tests.cases.app.therapist.appointments.notifications

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettings.Companion.defaultSettingsFor
import pro.qyoga.tests.assertions.shouldBeFragment
import pro.qyoga.tests.clients.api.Notifications
import pro.qyoga.tests.clients.api.TrainerAdvisorApis
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.NotificationsTestApi
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.appointments.notifications.NotificationsSettings


@DisplayName("Операция API получения фрагмента настроек уведомлений")
class GetNotificationsSettingsApiTest : QYogaAppIntegrationBaseTest() {

    private val notificationsTestApi = getBean<NotificationsTestApi>()

    @Test
    fun `должна отображать корректные день и время уведомления о заполнении расписания`() {
        // Given
        notificationsTestApi.enableNotifications(THE_THERAPIST_REF)
        val notificationsApi = TrainerAdvisorApis.Notifications.therapistApi(theTherapist.authCookie)

        // When
        val res = notificationsApi.getNotificationsSettings()

        // Then
        res shouldBeFragment NotificationsSettings.with(defaultSettingsFor(THE_THERAPIST_REF))
    }

}
