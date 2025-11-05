package pro.qyoga.tests.cases.core.appointments.notifications

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import pro.azhidkov.platform.java.time.Interval
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.core.appointments.notifications.fill_schedule.FillScheduleNotificationsSettingsRepo
import pro.qyoga.tests.fixture.backgrounds.UsersBackgrounds
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.test_apis.NotificationsTestApi
import pro.qyoga.tests.fixture.test_apis.UserTimeZonesTestApi
import pro.qyoga.tests.infra.web.QYogaAppBaseTest
import java.time.*


@DisplayName("Репозиторий настроек уведомлений о заполнении расписания")
class FillScheduleNotificationsRepoTest : QYogaAppBaseTest() {

    private val usersBackgrounds = getBean<UsersBackgrounds>()
    private val userTimeZonesTestApi = getBean<UserTimeZonesTestApi>()
    private val notificationsTestApi = getBean<NotificationsTestApi>()
    private val fillScheduleNotificationsSettingsRepo = getBean<FillScheduleNotificationsSettingsRepo>()

    @Nested
    @DisplayName("Метод поиска терапевтов для уведомления")
    inner class FindTherapistsForNotification {

        @Test
        fun `должен возвращать терапевтов у которых настроены уведомления на день и время соответствующие параметрам запроса`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            val nskTherapist = THE_THERAPIST_REF
            notificationsTestApi.createFillScheduleSettings(
                nskTherapist,
                notificationDay,
                LocalTime.of(10, 0)
            )

            val moscowTherapist = usersBackgrounds.registerNewTherapist().ref()
            userTimeZonesTestApi.setTimeZone(moscowTherapist, ZoneId.of("Europe/Moscow"))
            notificationsTestApi.createFillScheduleSettings(
                moscowTherapist,
                notificationDay,
                LocalTime.of(6, 0)
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.map { it.id } shouldContainExactlyInAnyOrder listOf(nskTherapist, moscowTherapist).map { it.id }
        }

        @Test
        fun `не должен возвращать терапевтов с отключенными уведомлениями`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                notificationDay,
                LocalTime.of(10, 0),
                enabled = false
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.shouldBeEmpty()
        }

        @Test
        fun `не должен возвращать терапевтов с настройками на другой день недели`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                DayOfWeek.TUESDAY, // другой день
                LocalTime.of(10, 0)
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.shouldBeEmpty()
        }

        @Test
        fun `не должен возвращать терапевтов с временем уведомления до начала интервала`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                notificationDay,
                LocalTime.of(9, 59) // на минуту раньше
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.shouldBeEmpty()
        }

        @Test
        fun `не должен возвращать терапевтов с временем уведомления после конца интервала`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                notificationDay,
                LocalTime.of(11, 1) // после конца интервала
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.shouldBeEmpty()
        }

        @Test
        fun `должен возвращать терапевтов с временем на границе начала интервала`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                notificationDay,
                LocalTime.of(10, 0) // ровно начало
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists shouldContainExactly listOf(THE_THERAPIST_REF)
        }

        @Test
        fun `должен возвращать терапевтов с временем на границе конца интервала`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                notificationDay,
                LocalTime.of(11, 0) // ровно конец
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists shouldContainExactly listOf(THE_THERAPIST_REF)
        }

        @Test
        fun `должен возвращать пустой список когда нет настроек уведомлений`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                asiaNovosibirskTimeZone
            ).withZoneSameInstant(ZoneId.of("UTC"))

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.shouldBeEmpty()
        }

        @Test
        fun `должен корректно работать с часовыми поясами отличающимися на несколько часов от UTC`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(10, 0),
                ZoneId.of("UTC")
            )

            // Токио UTC+9
            val tokyoTherapist = usersBackgrounds.registerNewTherapist().ref()
            userTimeZonesTestApi.setTimeZone(tokyoTherapist, ZoneId.of("Asia/Tokyo"))
            notificationsTestApi.createFillScheduleSettings(
                tokyoTherapist,
                notificationDay,
                LocalTime.of(19, 0)
            )

            // Нью-Йорк UTC-4/-5
            val nyTherapist = usersBackgrounds.registerNewTherapist().ref()
            userTimeZonesTestApi.setTimeZone(nyTherapist, ZoneId.of("America/New_York"))
            notificationsTestApi.createFillScheduleSettings(
                nyTherapist,
                notificationDay,
                LocalTime.of(6, 0)
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists.map { it.id } shouldContainExactlyInAnyOrder listOf(tokyoTherapist, nyTherapist).map { it.id }
        }

        @Test
        fun `должен корректно обрабатывать интервал уведомлений через полночь`() {
            // Given
            val notificationDay = DayOfWeek.MONDAY
            val notificationTime = ZonedDateTime.of(
                LocalDate.of(2025, 10, 14),
                LocalTime.of(23, 30),
                ZoneId.of("UTC")
            )

            userTimeZonesTestApi.setTimeZone(THE_THERAPIST_REF, ZoneId.of("UTC"))
            notificationsTestApi.createFillScheduleSettings(
                THE_THERAPIST_REF,
                notificationDay,
                LocalTime.of(23, 45)
            )

            // When
            val therapists = fillScheduleNotificationsSettingsRepo.findTherapistsToNotify(
                notificationDay,
                Interval.of(notificationTime.toLocalTime(), Duration.ofHours(1))
            )

            // Then
            therapists shouldContainExactly listOf(THE_THERAPIST_REF)
        }
    }

}
