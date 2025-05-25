package pro.qyoga.tests.cases.app.therapist.appointments.core

import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.http.HttpStatus
import pro.azhidkov.platform.spring.sdj.ergo.hydration.ref
import pro.azhidkov.platform.spring.sdj.ergo.hydration.resolveOrThrow
import pro.azhidkov.timezones.TimeZones
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController
import pro.qyoga.core.appointments.core.model.AppointmentStatus
import pro.qyoga.core.appointments.core.toEditRequest
import pro.qyoga.tests.assertions.shouldBePage
import pro.qyoga.tests.assertions.shouldHave
import pro.qyoga.tests.assertions.shouldHaveElement
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.backgrounds.AppointmentsBackgrounds
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.aAppointmentId
import pro.qyoga.tests.fixture.object_mothers.appointments.AppointmentsObjectMother.randomEditAppointmentRequest
import pro.qyoga.tests.fixture.object_mothers.appointments.randomAppointmentCost
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest
import pro.qyoga.tests.pages.publc.NotFoundErrorPage
import pro.qyoga.tests.pages.therapist.appointments.CalendarPage
import pro.qyoga.tests.pages.therapist.appointments.CreateAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentForm
import pro.qyoga.tests.pages.therapist.appointments.EditAppointmentPage
import pro.qyoga.tests.platform.html.HtmlPage
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId

@DisplayName("Страница редактирования записи на прием")
class EditAppointmentPageTest : QYogaAppIntegrationBaseTest() {

    private val timeZones = getBean<TimeZones>()
    private val appointmentBackgrounds = getBean<AppointmentsBackgrounds>()

    @Test
    fun `Страница редактирования только с обязательными полями записи на прием должна корректно отображаться и заполняться`() {
        // Сетап
        val appointment = appointmentBackgrounds.create()

        // Действие
        val document = theTherapist.appointments.getEditAppointmentPage(appointment.ref())

        // Проверка
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment.toEditRequest(timeZones::findById))
    }

    @Test
    fun `Страница редактирования со всеми заполененными полями записи на прием должна корректно отображаться и заполняться`() {
        // Сетап
        val appointment = appointmentBackgrounds.createFull()

        // Действие
        val document = theTherapist.appointments.getEditAppointmentPage(appointment.ref())

        // Проверка
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment.toEditRequest(timeZones::findById))
    }

    @Test
    fun `Отредактированная запись на прием только с обязательными полями должна сохраняться`() {
        // Сетап
        val appointment = appointmentBackgrounds.create()
        val newAppointmentType = backgrounds.appointmentTypes.createAppointmentType()
        val therapeuticTask = backgrounds.therapeuticTasks.createTherapeuticTask()
        val editedAppointment =
            AppointmentsObjectMother.appointmentPatchRequest(
                appointment,
                newAppointmentType.ref(),
                newAppointmentType.name,
                therapeuticTask = therapeuticTask.ref(),
                cost = randomAppointmentCost(),
                payed = true,
                place = randomCyrillicWord(),
                comment = randomCyrillicWord()
            )

        // Действие
        val response = theTherapist.appointments.editAppointment(appointment.ref(), editedAppointment)

        // Проверка
        response.statusCode() shouldBe HttpStatus.OK.value()
        response shouldBePage CalendarPage

        val persistedAppointment =
            appointmentBackgrounds.getDaySchedule(editedAppointment.dateTime.toLocalDate()).single()
        persistedAppointment.shouldMatch(editedAppointment)
    }

    @Test
    fun `Отредактированная запись на прием со всеми полями должна сохраняться`() {
        // Сетап
        val appointment = appointmentBackgrounds.createFull()
        val editedAppointment =
            AppointmentsObjectMother.appointmentPatchRequest(
                appointment,
                appointment.typeRef,
                appointment.typeRef.resolveOrThrow().name,
                therapeuticTask = null,
                cost = null,
                payed = false,
                place = null,
                comment = null
            )

        // Действие
        val response = theTherapist.appointments.editAppointment(appointment.ref(), editedAppointment)

        // Проверка
        response.statusCode() shouldBe HttpStatus.OK.value()
        response shouldBePage CalendarPage

        val persistedAppointment = appointmentBackgrounds.findAll().single()
        persistedAppointment shouldMatch editedAppointment
    }

    @Test
    fun `При запросе страницы редактирования несуществующего приема должна возвращаться страница с ошибкой 404`() {
        // Действие
        val document =
            theTherapist.appointments.getEditAppointmentPage(aAppointmentId(), expectedStatus = HttpStatus.NOT_FOUND)

        // Проверка
        document shouldBePage (NotFoundErrorPage as HtmlPage)
    }

    @Test
    fun `При запросе на редактировании несуществующего приема должна возвращаться страница с ошибкой 404`() {
        // Сетап
        val editAppointmentRequest = randomEditAppointmentRequest()

        // Действие
        val response = theTherapist.appointments.editAppointment(
            aAppointmentId(), editAppointmentRequest
        )

        // Проверка
        response.statusCode() shouldBe HttpStatus.NOT_FOUND.value()
        response shouldBePage NotFoundErrorPage
    }

    @Test
    fun `Создание приема, пересекающегося с существующим, должно завершиться ошибкой проверки`() {
        // Сетап
        val appointmentsDate = LocalDate.of(2024, 2, 11)
        val appointmentsBaseTime = LocalTime.of(9, 0)
        val zoneId = ZoneId.of("Asia/Novosibirsk")
        val existingAppointmentDuration = Duration.ofHours(1)

        val existingAppointment = appointmentBackgrounds.create(
            dateTime = appointmentsDate.atTime(appointmentsBaseTime),
            timeZone = zoneId,
            duration = existingAppointmentDuration
        )
        val nextNotRescheduledAppointment = appointmentBackgrounds.create(
            dateTime = appointmentsDate.atTime(appointmentsBaseTime).plus(existingAppointmentDuration),
            timeZone = zoneId,
            duration = existingAppointmentDuration
        )
        val rescheduleAppointmentRequest = nextNotRescheduledAppointment.copy(dateTime = existingAppointment.dateTime)
            .toEditRequest(timeZones::findById)

        // Действие
        val document = theTherapist.appointments.editAppointmentForError(
            nextNotRescheduledAppointment.ref(),
            rescheduleAppointmentRequest
        )

        // Проверка
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(rescheduleAppointmentRequest)
        document shouldHaveElement CreateAppointmentForm.appointmentsIntersectionErrorMessage

        val storedNextAppointment = appointmentBackgrounds.findById(nextNotRescheduledAppointment.id)!!
        storedNextAppointment shouldMatch nextNotRescheduledAppointment
    }

    @Test
    fun `Удаление записи на прием должно быть постоянным и перенаправлять на указанный календарный день`() {
        // Сетап
        val appointment = appointmentBackgrounds.create()

        // Действие
        val response = theTherapist.appointments.delete(appointment.ref(), appointment.wallClockDateTime.toLocalDate())

        // Проверка
        response.statusCode shouldBe HttpStatus.OK.value()
        response.header("HX-Location") shouldBe SchedulePageController.DATE_PATH.replace(
            "{date}",
            appointment.wallClockDateTime.toLocalDate().toString()
        )

        appointmentBackgrounds.getDaySchedule(appointment.wallClockDateTime.toLocalDate()).shouldBeEmpty()
    }

    @Test
    fun `Запись на прием со статусом 'Клиент пришел' должна отображаться корректно`() {
        // Сетап
        val appointment = appointmentBackgrounds.create(appointmentStatus = AppointmentStatus.CLIENT_CAME)

        // Действие
        val document = theTherapist.appointments.getEditAppointmentPage(appointment.ref())

        // Проверка
        document shouldBePage EditAppointmentPage
        document shouldHave EditAppointmentForm.formPrefilledWith(appointment.toEditRequest(timeZones::findById))
    }

}

