package pro.qyoga.core.therapy.exercises.model

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.file_storage.api.FileMetaData


@Table("exercise_steps")
data class ExerciseStep(
    val description: String,
    val imageId: AggregateReference<FileMetaData, Long>?
) {

    fun withImage(imageId: AggregateReference<FileMetaData, Long>?) =
        copy(imageId = imageId)

}