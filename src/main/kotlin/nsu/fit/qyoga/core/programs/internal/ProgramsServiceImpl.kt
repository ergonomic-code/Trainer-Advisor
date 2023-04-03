package nsu.fit.qyoga.core.programs.internal

import nsu.fit.qyoga.core.programs.api.ProgramsService
import nsu.fit.qyoga.core.programs.api.dtos.ProgramDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class ProgramsServiceImpl(
    private val programsRepo: ProgramsRepo
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
}
