package pro.qyoga.tests.scripts.therapist.appointemnts

import com.codeborne.selenide.Selenide.open
import pro.qyoga.app.therapist.appointments.core.schedule.SchedulePageController


fun goToSchedulePage() {
    open(SchedulePageController.PATH)
}