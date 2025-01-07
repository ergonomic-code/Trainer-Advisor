package pro.qyoga.tests.fixture.object_mothers.therapy.therapeutic_tasks

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.therapy.therapeutic_tasks.model.TherapeuticTask
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_ID
import java.util.*

object TherapeuticTasksObjectMother {

    fun therapeuticTask(
        name: String = randomCyrillicWord(),
        owner: UUID = THE_THERAPIST_ID
    ) = TherapeuticTask(
        AggregateReference.to(owner),
        name
    )

}