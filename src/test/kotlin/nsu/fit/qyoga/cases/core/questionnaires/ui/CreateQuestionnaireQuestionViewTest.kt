package nsu.fit.qyoga.cases.core.questionnaires.ui

import io.kotest.matchers.shouldBe
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.infra.QYogaAppTestBase
import nsu.fit.qyoga.infra.db.DbInitializer
import org.jsoup.Jsoup
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired

class CreateQuestionnaireQuestionViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-empty-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can add question to questionnaire`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            patch("/therapist/questionnaires/edit/add-question")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
                node("#question1") { exists() }
                node("#question1Header") { exists() }
                node("#question1Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    @Test
    fun `QYoga can add question in empty questionnaire`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0")
            patch("/therapist/questionnaires/edit/add-question")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
                node("#question1") { notExists() }
                node("#question1Header") { notExists() }
                node("#question1Body") { notExists() }
            }
        }
    }

    @Test
    fun `QYoga can't add question if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            patch("/therapist/questionnaires/edit/add-question")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can delete question from questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/0")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga ignore delete question if it not in questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            delete("/therapist/questionnaires/edit/question/-1")
            get("/therapist/questionnaires/edit")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't delete question if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            delete("/therapist/questionnaires/edit/question/0")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can change question type`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/change-type")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".typeSEVERAL") { hasAttribute("selected") }
                node(".typeSINGLE") { notHasAttribute("selected") }
                node(".typeTEXT") { notHasAttribute("selected") }
                node(".typeRANGE") { notHasAttribute("selected") }
            }
        }
    }

    @Test
    fun `QYoga can't change question type if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/change-type")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can't change question type it not in questionnaire`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/change-type")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранный вопрос не найден Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can update question`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can't update question if it not in session's questionnaire`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/1/update")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранный вопрос не найден Перезагрузить")
                }
            }
        }
    }

    @Test
    fun `QYoga can't update question type if questionnaire not in session`() {
        Given {
            authorized()
        } When {
            params(setParams())
            post("/therapist/questionnaires/edit/question/0/update")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#layoutSidenav_content") {
                    exists()
                    hasText("Ошибка извлечения опросника из сессии")
                }
            }
        }
    }

    @Test
    fun `QYoga can't update question type it not in questionnaire`() {
        Given {
            authorized()
        } When {
            post("/therapist/questionnaires/new")
            params(setParams())
            post("/therapist/questionnaires/edit/question/-1/update")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#reload-page-btn") {
                    exists()
                    hasText("Перезагрузить")
                }
                node(".error-text") {
                    exists()
                    hasText("Выбранный вопрос не найден Перезагрузить")
                }
            }
        }
    }

    fun setParams(): Map<String, String> {
        return mutableMapOf(
            "id" to "1",
            "title" to "test",
            "question[0].id" to "0",
            "question[0].title" to "questions title",
            "question[0].questionType" to "SEVERAL",
            "question[0].answers[0].id" to "0",
            "question[0].answers[0].score" to "1",
            "question[0].answers[0].title" to "answer title",
            "question[1].id" to "1",
            "question[1].title" to "questions title",
            "question[1].questionType" to "SEVERAL",
            "question[1].answers[0].id" to "0",
            "question[1].answers[0].score" to "1",
            "question[1].answers[0].title" to "answer title"
        )
    }
}
