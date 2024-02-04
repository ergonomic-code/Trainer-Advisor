package pro.qyoga.tests.fixture.appointments

import pro.qyoga.core.appointments.core.dtos.EditAppointmentRequest
import pro.qyoga.core.appointments.types.model.AppointmentTypeRef
import pro.qyoga.core.clients.cards.api.ClientRef
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTaskRef
import pro.qyoga.tests.fixture.data.*
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneId
import kotlin.random.Random

object AppointmentsObjectMother {

    fun randomEditAppointmentRequest(
        client: ClientRef,
        typeId: AppointmentTypeRef? = null,
        typeTitle: String = randomCyrillicWord(),
        therapeuticTask: TherapeuticTaskRef,
        timeZone: ZoneId = randomTimeZone(),
        place: String? = null,
        cost: Int? = null,
        payed: Boolean? = null,
        comment: String? = null
    ): EditAppointmentRequest {
        return EditAppointmentRequest(
            client,
            typeId,
            typeTitle,
            therapeuticTask,
            randomAppointmentDate(),
            timeZone,
            place,
            cost,
            payed,
            comment
        )
    }

    fun randomFullEditAppointmentRequest(
        client: ClientRef,
        typeTitle: String = randomCyrillicWord(),
        therapeuticTask: TherapeuticTaskRef,
        place: String = randomSentence(),
        cost: Int = randomAppointmentCost(),
        payed: Boolean = Random.nextBoolean(),
        comment: String = randomSentence()
    ) = randomEditAppointmentRequest(
        client,
        typeTitle = typeTitle,
        therapeuticTask = therapeuticTask,
        place = place,
        cost = cost,
        payed = payed,
        comment = comment
    )

}

fun randomAppointmentDate(): LocalDateTime =
    randomLocalDate(LocalDate.now(), Duration.ofDays(30))
        .atTime(randomWorkingTime())

fun randomAppointmentCost(): Int = Random.nextInt(0, 10_000)
