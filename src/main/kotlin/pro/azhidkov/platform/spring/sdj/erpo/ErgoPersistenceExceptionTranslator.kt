package pro.azhidkov.platform.spring.sdj.erpo

import org.springframework.dao.DataAccessException
import org.springframework.dao.support.PersistenceExceptionTranslator
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import org.springframework.stereotype.Component


@Component
class ErgoPersistenceExceptionTranslator : PersistenceExceptionTranslator {

    override fun translateExceptionIfPossible(ex: RuntimeException): DataAccessException? =
        (ex as? DbActionExecutionException)?.cause as? DataAccessException

}