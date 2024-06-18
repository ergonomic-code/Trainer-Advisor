package pro.qyoga.app.therapist.therapy.programs.list

import org.springframework.stereotype.Component
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.file_storage.api.StoredFileInputStream
import pro.qyoga.core.therapy.exercises.ExercisesService
import pro.qyoga.core.therapy.programs.ProgramDocxGenerator
import pro.qyoga.core.therapy.programs.ProgramsRepo
import pro.qyoga.core.therapy.programs.findDocxById

@Component
class GenerateProgramDocx(
    private val programsRepo: ProgramsRepo,
    private val exercisesService: ExercisesService,
) : (Long) -> StoredFileInputStream? {

    override fun invoke(programId: Long): StoredFileInputStream? {
        val program = programsRepo.findDocxById(programId)
            ?: return null
        val docxInputStream = ProgramDocxGenerator.generateDocx(program) { exerciseId, stepIdx ->
            exercisesService.getStepImage(exerciseId, stepIdx)
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