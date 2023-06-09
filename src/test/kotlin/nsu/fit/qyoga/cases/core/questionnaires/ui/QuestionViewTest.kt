package nsu.fit.qyoga.cases.core.questionnaires.ui

import nsu.fit.qyoga.infra.db.DbInitializer
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

class QuestionViewTest {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }
}
