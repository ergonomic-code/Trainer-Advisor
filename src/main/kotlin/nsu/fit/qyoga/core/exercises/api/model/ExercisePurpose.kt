package nsu.fit.qyoga.core.exercises.api.model

import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table

// data пришлось убрать из-за бага в SDJ
// В SDJ 3.0.2 (и 6) летит IndexOutOfBoundsException в
// org.springframework.data.mapping.model.KotlinCopyMethod.shouldUsePublicCopyMethod(KotlinCopyMethod.java:155)
// при попытке сохранения дата класса
@Suppress("UseDataClass")
@Table("exercise_purposes")
class ExercisePurpose(
        val purposeId: AggregateReference<TherapeuticPurpose, Long>
) {

    constructor(therapeuticPurpose: TherapeuticPurpose) : this(AggregateReference.to(therapeuticPurpose.id))

}
