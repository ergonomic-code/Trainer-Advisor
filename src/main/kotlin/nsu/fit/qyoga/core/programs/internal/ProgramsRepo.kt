package nsu.fit.qyoga.core.programs.internal

import nsu.fit.qyoga.core.programs.api.Program
import nsu.fit.qyoga.core.programs.api.dtos.ProgramDto
import nsu.fit.qyoga.core.programs.api.dtos.ProgramSearchDto
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.data.repository.PagingAndSortingRepository
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = false)
interface ProgramsRepo : CrudRepository<Program, Long>, PagingAndSortingRepository<Program, Long> {

    @Query(
        """
        SELECT p.id as id, title, date, tp.purpose as purpose
        FROM programs p
            INNER JOIN purpose_programs pp ON p.id = pp.program_id
            INNER JOIN therapeutic_purposes tp ON tp.id = pp.purpose_id
        WHERE (p.title LIKE '%' || :#{#search.title ?: ""} || '%' OR :#{#search.title ?: ""} IS NULL)
            AND (p.date LIKE '%' || :#{#search.date ?: ""} || '%' OR :#{#search.date ?: ""} IS NULL)
            AND (tp.purpose LIKE '%' || :#{#search.therapeuticPurpose ?: ""} || '%' OR :#{#search.therapeuticPurpose ?: ""} IS NULL)
        ORDER BY p.title
        LIMIT :pageSize OFFSET :offset
    """
    )
    fun getProgramsByFilter(
        @Param("search") search: ProgramSearchDto,
        offset: Int,
        pageSize: Int
    ): List<ProgramDto>

    @Query(
        """
        SELECT count(*) 
        FROM programs p
            INNER JOIN purpose_programs pp ON p.id = pp.program_id
            INNER JOIN therapeutic_purposes tp ON tp.id = pp.purpose_id
        WHERE (p.title LIKE '%' || :#{#search.title ?: ""} || '%' OR :#{#search.title ?: ""} IS NULL)
            AND (p.date LIKE '%' || :#{#search.date ?: ""} || '%' OR :#{#search.date ?: ""} IS NULL)
            AND (tp.purpose LIKE '%' || :#{#search.therapeuticPurpose ?: ""} || '%' OR :#{#search.therapeuticPurpose ?: ""} IS NULL)
    """
    )
    fun countPrograms(
        @Param("search") search: ProgramSearchDto,
    ): Long
}
