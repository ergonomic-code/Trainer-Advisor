package pro.qyoga.tests.fixture.therapy.therapeutic_tasks

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.therapy.therapeutic_tasks.api.TherapeuticTask
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.therapists.THE_THERAPIST_ID

object TherapeuticTasksObjectMother {

    fun therapeuticTask(
        name: String = randomCyrillicWord(),
        owner: Long = THE_THERAPIST_ID
    ) = TherapeuticTask(
        AggregateReference.to(owner),
        name
    )

}