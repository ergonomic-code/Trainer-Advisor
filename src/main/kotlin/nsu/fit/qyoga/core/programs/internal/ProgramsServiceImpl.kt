package nsu.fit.qyoga.core.programs.internal

import nsu.fit.platform.errors.ResourceNotFound
import nsu.fit.qyoga.core.exercises.api.ImagesService
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseStepDto
import nsu.fit.qyoga.core.exercises.api.dtos.ModifiableExerciseDto
import nsu.fit.qyoga.core.exercises.api.model.toMultipartFile
import nsu.fit.qyoga.core.exercises.internal.ExerciseStepsRepo
import nsu.fit.qyoga.core.exercises.internal.ExercisesRepo
import nsu.fit.qyoga.core.programs.api.ProgramsService
import nsu.fit.qyoga.core.programs.api.dtos.ProgramDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramWithExercisesDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.io.FileOutputStream

@Service
class ProgramsServiceImpl(
    private val programsRepo: ProgramsRepo,
    private val exercisesRepo: ExercisesRepo,
    private val exerciseStepsRepo: ExerciseStepsRepo,
    private val imagesService: ImagesService
) : ProgramsService {

    override fun getPrograms(searchDto: ProgramSearchDto, pageable: Pageable): Page<ProgramDto> {
        val result = programsRepo.getProgramsByFilter(
            searchDto,
            pageable.pageNumber * pageable.pageSize,
            pageable.pageSize
        )
        val count = programsRepo.countPrograms(searchDto)
        return PageImpl(result, pageable, count)
    }

    override fun getProgramById(id: Long): ProgramWithExercisesDto {
        val plainProgram =
            programsRepo.getProgramsById(id) ?: throw ResourceNotFound("No existing exercise with id = $id")
        val exercises = exercisesRepo.getExercisesByProgram(plainProgram.id)
        val exercisesWithSteps = exercises.map { exerciseDto ->
            val steps = exerciseStepsRepo.findAllByExerciseId(exerciseDto.id).map {
                ExerciseStepDto(it.imageId, it.description, imagesService.getImage(it.imageId)?.toMultipartFile())
            }
            ModifiableExerciseDto(
                exerciseDto.id,
                exerciseDto.title,
                exerciseDto.description,
                exerciseDto.indications,
                exerciseDto.contradictions,
                exerciseDto.duration,
                exerciseDto.type,
                exerciseDto.purpose,
                steps.toMutableList()
            )
        }
        return ProgramWithExercisesDto(
            id = plainProgram.id,
            title = plainProgram.title,
            date = plainProgram.date,
            purpose = plainProgram.purpose,
            exercises = exercisesWithSteps
        )
    }

    override fun downloadProgram(program: ProgramWithExercisesDto) {
        val file = generateProgramPoi(program, imagesService::getImages)
        val ous = FileOutputStream("./${program.title}.${program.date.substring(0, 10)}.docx.")
        file.copyTo(ous)
    }
}
