package pro.qyoga.infra.db

import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import javax.sql.DataSource

class DbInitializer(
    private val dataSource: DataSource
) {

    fun executeScripts(vararg scripts: String) {
        dataSource.connection.use {
            scripts.forEach { script ->
                ScriptUtils.executeSqlScript(it, ClassPathResource(script))
            }
        }
    }

}
