package nsu.fit.qyoga.cases.core.exercises.ui

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class ExerciseViewTest : QYogaAppTestBase() {

    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/exercises-init-script.sql" to "dataSource",
            "/db/migration/demo/V23050906__insert_exercises_data.sql" to "dataSource",
        )
    }

    @Test
    fun `QYoga returns exercise-search page with exercise table`() {
        When {
            get("/exercises")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#exercisesTable") { exists() }
                node("#exercises-list") { exists() }
                node("td") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns exercise-search page with input fields`() {
        When {
            get("/exercises")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#titleFilter") { exists() }
                node("#contradictionFilter") { exists() }
                node("#therapeuticPurposeFilter") { exists() }
                node("#exerciseTypeFilter") { exists() }
            }
        }
    }

    @Test
    fun `QYoga returns exercises table with pagination`() {
        When {
            get("/exercises/search")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            Assertions.assertThatSpec(body) {
                node("#exercisesTable") { exists() }
                node("#exercises-list") { exists() }
                node("td") { exists() }
                node("#pagination") { exists() }
            }
        }
    }
}
