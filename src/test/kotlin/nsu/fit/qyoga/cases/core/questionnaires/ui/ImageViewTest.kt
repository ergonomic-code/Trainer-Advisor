package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.kotest.matchers.shouldNotBe
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

class ImageViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-empty-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can't return non existing image`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/image/-1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Изображение не найдено Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga return image on success request`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/images/testImage.png"))
            post("/therapist/questionnaires/edit/question/0/answer/0/add-image")
            get("/therapist/questionnaires/image/1")
        } Then {
            extract().body().asString().length shouldNotBe 0
        }
    }
}
