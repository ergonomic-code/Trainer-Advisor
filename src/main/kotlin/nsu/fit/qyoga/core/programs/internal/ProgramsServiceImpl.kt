package nsu.fit.qyoga.core.programs.internal

import nsu.fit.platform.errors.ResourceNotFound
import nsu.fit.qyoga.core.exercises.internal.ExercisesRepo
import nsu.fit.qyoga.core.programs.api.ProgramsService
import nsu.fit.qyoga.core.programs.api.dtos.ProgramDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramWithExercisesDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProgramsServiceImpl(
    private val programsRepo: ProgramsRepo,
    private val exercisesRepo: ExercisesRepo
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
        return ProgramWithExercisesDto(
            id = plainProgram.id,
            title = plainProgram.title,
            date = plainProgram.date,
            purpose = plainProgram.purpose,
            exercises = exercises
        )
    }
}
