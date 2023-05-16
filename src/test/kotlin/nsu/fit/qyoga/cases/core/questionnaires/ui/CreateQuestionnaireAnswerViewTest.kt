package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.io.File

class CreateQuestionnaireAnswerViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can save answer image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/add-image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".questionImageId") { exists() }
                node(".questionImage") { exists() }
                node(".questionDeleteImage") { exists() }
            }
        }
    }
}
