package pro.qyoga.core.therapy.exercises

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Import
import pro.qyoga.platform.file_storage.FilesStorageConfig
import pro.qyoga.platform.file_storage.internal.FilesRepo
import pro.qyoga.platform.file_storage.internal.FilesStorageImpl


@Import(FilesStorageConfig::class)
@Configuration
class ExercisesConfig(
    private val filesRepo: FilesRepo
) {

    @Bean
    fun exerciseStepsImagesStorage() =
        FilesStorageImpl(filesRepo, "exerciseSteps")

}