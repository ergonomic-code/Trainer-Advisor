package pro.qyoga.core.appointments.core

import pro.azhidkov.platform.kotlin.LabeledEnum


enum class AppointmentStatus(
    override val label: String
) : LabeledEnum {

    PENDING("Ожидается клиент"),
    CLIENT_CAME("Клиент пришёл"),
    CLIENT_DO_NOT_CAME("Клиент не пришёл")

}