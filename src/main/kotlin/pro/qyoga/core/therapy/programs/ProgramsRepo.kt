package pro.qyoga.core.therapy.programs

import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.mapping.model.BasicPersistentEntity
import org.springframework.data.util.TypeInformation
import org.springframework.stereotype.Repository
import pro.qyoga.core.therapy.programs.model.Program
import pro.qyoga.platform.spring.sdj.erpo.ErgoRepository
import pro.qyoga.platform.spring.sdj.sortBy

@Repository
class ProgramsRepo(
    override val jdbcAggregateTemplate: JdbcAggregateOperations,
    jdbcConverter: JdbcConverter
) : ErgoRepository<Program, Long>(
    jdbcAggregateTemplate,
    BasicPersistentEntity(TypeInformation.of(Program::class.java)),
    jdbcConverter
) {

    object Page {
        val firstTenByTitle = PageRequest.of(0, 10, sortBy(Program::title))
    }

}
