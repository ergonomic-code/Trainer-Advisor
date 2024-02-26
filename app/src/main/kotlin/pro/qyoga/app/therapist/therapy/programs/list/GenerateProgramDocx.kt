package pro.qyoga.app.therapist.therapy.programs.list

import org.springframework.stereotype.Component
import pro.azhidkov.platform.file_storage.api.FileMetaData
import pro.azhidkov.platform.file_storage.api.FilesStorage
import pro.azhidkov.platform.file_storage.api.StoredFileInputStream
import pro.qyoga.core.therapy.programs.ProgramDocxGenerator
import pro.qyoga.core.therapy.programs.impl.ProgramsRepo
import pro.qyoga.core.therapy.programs.impl.findDocxOrNull

@Component
class GenerateProgramDocx(
    private val programsRepo: ProgramsRepo,
    private val exerciseStepsImagesStorage: FilesStorage,
) : (Long) -> StoredFileInputStream? {

    override fun invoke(programId: Long): StoredFileInputStream? {
        val program = programsRepo.findDocxOrNull(programId)
            ?: return null
        val docxInputStream = ProgramDocxGenerator.generateDocx(program) { imageId ->
            if (imageId != null) exerciseStepsImagesStorage.findByIdOrNull(imageId) else null
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