package nsu.fit.qyoga.cases.core.exercises.ui

import io.github.ulfs.assertj.jsoup.Assertions
import io.restassured.module.kotlin.extensions.Extract
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import nsu.fit.qyoga.platform.assertLinkValid
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

private const val EXERCISE_SEARCH_ENDPOINT = "/therapist/exercises/search"

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
        val body = Given {
            authorized()
        } When {
            get("/therapist/exercises")
        } Extract {
            Jsoup.parse(this.body().asString())
        }

        Assertions.assertThatSpec(body) {
            node("#titleFilter") { exists() }
            node("#contradictionFilter") { exists() }
            node("#therapeuticPurposeFilter") { exists() }
            node("#exerciseTypeFilter") { exists() }
            node("form") {
                attribute("hx-get") { hasText(EXERCISE_SEARCH_ENDPOINT) }
            }

            node("#exercisesTable") { exists() }
            node("#exercises-list") { exists() }
            node("td") { exists() }
        }

        assertLinkValid(body, "Новое упражнение")
    }

    @Test
    fun `QYoga returns exercises table with pagination`() {
        Given {
            authorized()
        } When {
            get(EXERCISE_SEARCH_ENDPOINT)
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
