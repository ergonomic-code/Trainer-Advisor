package pro.qyoga.app.therapist.therapy.programs.list

import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.programs.ProgramDocxGenerator
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.model.fetchExercises
import pro.qyoga.platform.file_storage.api.FileMetaData
import pro.qyoga.platform.file_storage.api.StoredFileInputStream


@Component
class GenerateProgramDocx(
    private val programsRepo: ProgramsRepo,
    private val exercisesService: ExercisesService,
    private val jdbcAggregateTemplate: JdbcAggregateTemplate
) : (Long) -> StoredFileInputStream? {

    override fun invoke(programId: Long): StoredFileInputStream? {
        val program = programsRepo.findByIdOrNull(programId)
            ?.fetchExercises(jdbcAggregateTemplate)
            ?: return null

        val docxInputStream = ProgramDocxGenerator.generateDocx(program) { (exerciseId, stepIdx) ->
            exercisesService.getStepImage(
                exerciseId,
                stepIdx
            )
        }

        return StoredFileInputStream(
            FileMetaData(
                "${program.title}.docx",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
                -1L
            ), docxInputStream
        )
    }

}