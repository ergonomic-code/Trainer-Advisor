package pro.qyoga.core.therapy.exercises.api.model

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.platform.file_storage.api.StoredFile


@Table("exercise_steps")
data class ExerciseStep(
    val description: String,
    val imageId: AggregateReference<StoredFile, Long>?
) {

    fun withImage(imageId: AggregateReference<StoredFile, Long>?) =
        copy(imageId = imageId)

}