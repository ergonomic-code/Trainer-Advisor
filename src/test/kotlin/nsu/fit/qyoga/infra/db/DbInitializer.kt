package nsu.fit.qyoga.infra.db

import org.springframework.context.ApplicationContext
import org.springframework.core.io.ClassPathResource
import org.springframework.jdbc.datasource.init.ScriptUtils
import javax.sql.DataSource

class DbInitializer(
    private val applicationTestContext: ApplicationContext
) {

    fun executeScripts(vararg scripts: Pair<String, String>) {
        val datasourceScripts = scripts.groupBy(Pair<String, String>::second) { it.first }
        datasourceScripts
            .forEach { (dsBeanName, scripts) ->
                val ds = applicationTestContext.getBean(dsBeanName) as DataSource
                ds.connection.use {
                    scripts.forEach { script ->
                        ScriptUtils.executeSqlScript(it, ClassPathResource(script))
                    }
                }
            }
    }

}
