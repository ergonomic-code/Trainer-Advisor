package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.java.time.toLocalTimeString
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.qyoga.app.therapist.appointments.core.edit.view_model.SourceItem
import pro.qyoga.app.therapist.appointments.core.edit.view_model.toQueryParamStr
import pro.qyoga.core.calendar.ical.model.LocalizedICalCalendarItem
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.clients.TherapistClient
import pro.qyoga.tests.fixture.data.asiaNovosibirskTimeZone
import pro.qyoga.tests.fixture.data.randomWorkingTime
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomFullEditAppointmentRequest
import pro.qyoga.tests.fixture.object_mothers.calendars.CalendarsObjectMother.aLocalizedCalendarItem
import pro.qyoga.tests.fixture.presets.CalendarsFixturePresets
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentPage
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentForm
import pro.qyoga.tests.platform.instancio.KSelect.Companion.field
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

private val aDate = LocalDate.now()
private val aTime = LocalTime.now()
    .truncatedTo(ChronoUnit.HOURS)
    .plusHours(3)


@DisplayName("Страница создания приёма")
class CreateAppointmentPageTest : QYogaAppIntegrationBaseTest() {

    private val calendarsFixturePresets = getBean<CalendarsFixturePresets>()

    @Test
    fun `должна рендерится корректно`() {
        // Сетап
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.appointments.getCreateAppointmentPage()

        // Проверка
        document shouldBePage CreateAppointmentPage
    }

    @Test
    fun `должна предзаполнять поля даты и времени, если они были переданы в запросе`() {
        // Сетап
        val aDateTime = LocalDate.now().atTime(randomWorkingTime())
        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.appointments.getCreateAppointmentPage(aDateTime)

        // Проверка
        CreateAppointmentForm.dateTime.value(document) shouldBe aDateTime.toString()
    }

    @Test
    fun `должна сохранять приём только с обязательными полями`() {
        // Сетап
        val client = backgrounds.clients.createClients(1).single()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val editAppointmentRequest = AppointmentsObjectMother.randomEditAppointmentRequest(
            client = client.ref(),
            therapeuticTask = therapeuticTask.ref()
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val response = therapist.appointments.createAppointment(editAppointmentRequest)

        // Проверка
        response.statusCode() shouldBe HttpStatus.OK.value()

        val storedAppointment =
            backgrounds.appointments.getDaySchedule(editAppointmentRequest.dateTime.toLocalDate()).single()
        storedAppointment.shouldMatch(editAppointmentRequest)
    }

    @Test
    fun `должна сохранять приём со всеми полями`() {
        // Сетап
        val client = backgrounds.clients.createClients(1).single()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val editAppointmentRequest = randomFullEditAppointmentRequest(
            client = client.ref(),
            therapeuticTask = therapeuticTask.ref()
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val response = therapist.appointments.createAppointment(editAppointmentRequest)

        // Проверка
        response.statusCode() shouldBe HttpStatus.OK.value()

        val storedAppointment =
            backgrounds.appointments.getDaySchedule(editAppointmentRequest.dateTime.toLocalDate()).single()
        storedAppointment.shouldMatch(editAppointmentRequest)
        TODO("externalId")
    }

    @Test
    fun `должна возвращять ошибку при попытке создать приём пересекающийся с существующим`() {
        // Сетап
        val appointmentsDate = aDate
        val appointmentsBaseTime = aTime
        val zoneId = ZoneId.of("Asia/Novosibirsk")
        val existingAppointmentDuration = Duration.ofHours(1)

        backgrounds.appointments.create(
            dateTime = appointmentsDate.atTime(appointmentsBaseTime),
            timeZone = zoneId,
            duration = existingAppointmentDuration
        )
        val newAppointmentRequest = AppointmentsObjectMother.randomEditAppointmentRequest(
            client = backgrounds.clients.createClients(1).single().ref(),
            dateTime = appointmentsDate.atTime(appointmentsBaseTime.plus(existingAppointmentDuration.dividedBy(2))),
            timeZone = zoneId,
        )

        val therapist = TherapistClient.loginAsTheTherapist()

        // Действие
        val document = therapist.appointments.createAppointmentForError(newAppointmentRequest)

        // Проверка
        document shouldBePage CreateAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(newAppointmentRequest)
        document shouldHaveElement CreateAppointmentForm.appointmentsIntersectionErrorMessage
    }

    @Test
    @DisplayName("должна предзаполнять дату, время, длительность и идентификатор события источника данными из события ics-календаря, если его ид был передан в запросе") // длина имени файла с лямбдой превышает ограничение Линукса
    fun createAppointmentWithIcsEventId() {
        // Сетап
        val event = aLocalizedCalendarItem {
            set(field(LocalizedICalCalendarItem::dateTime), aDate.atTime(aTime))
            set(field(LocalizedICalCalendarItem::duration), Duration.ofMinutes(75))
        }
        val (_, eventId) = calendarsFixturePresets.createICalCalendarWithSingleEvent(event, asiaNovosibirskTimeZone)

        // Действие
        val document = theTherapist.appointments.getCreateAppointmentPage(
            dateTime = event.dateTime,
            sourceItem = SourceItem.icsEvent(eventId)
        )

        // Проверка
        CreateAppointmentForm.dateTime.value(document) shouldBe event.dateTime.toString()
        CreateAppointmentForm.duration.value(document) shouldBe event.duration.toLocalTimeString()
        CreateAppointmentForm.externalIdInput.value(document) shouldBe eventId.toQueryParamStr()
    }

}