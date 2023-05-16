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

class CreateQuestionnaireViewTest : QYogaAppTestBase() {
    @Autowired
    lateinit var dbInitializer: DbInitializer

    @BeforeEach
    fun setupDb() {
        dbInitializer.executeScripts(
            "/db/questionnaires/questionnaires-init-script.sql" to "dataSource",
            "db/questionnaires/questionnaires-insert-single-questionnaire.sql" to "dataSource"
        )
    }

    @Test
    fun `QYoga can edit questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/1/edit")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions") { exists() }
                node("#questionnaireTitle") {
                    attribute("value") { hasText("title") }
                }
                node("#question0") { notExists() }
                node("#question0Header") { notExists() }
                node("#question0Body") { notExists()  }
                node("#answer0") { notExists()  }
            }
        }
    }

    @Test
    fun `QYoga can create new questionnaire`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions") { exists() }
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    /*@Test
    fun `QYoga can save answer image`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/db/questionnaires/testLargeImage.jpg"))
            param("questionIndex", 0)
            param("answerIndex", 0)
            post("/questionnaires/answer/1/image")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".answerImageId") { exists() }
                node(".answerImage") { exists() }
                node(".answerDeleteImage") { exists() }
            }
        }
    }*/

    /*@Test
    fun `QYoga can delete image`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            contentType(ContentType.MULTIPART)
            multiPart(File("src/test/resources/db/questionnaires/testLargeImage.jpg"))
            param("questionIndex", 0)
            param("answerIndex", 0)
            post("/questionnaires/answer/1/image")
            delete("/questionnaires/image/1")
        } Then {
            extract().body().asString() shouldBe ""
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }*/

    /*@Test
    fun `QYoga can delete answer from question`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            get("/questionnaires/1/edit/add-question")
            delete("/questionnaires/1/edit/question/1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question1") { notExists() }
                node("#question1Header") { notExists() }
                node("#question1Body") { notExists() }
                node("#answer1") { notExists() }

                node("#question2") { exists() }
                node("#question2Header") { exists() }
                node("#question2Body") { exists() }
                node("#answer2") { exists() }
            }
        }
    }*/

    /*@Test
    fun `QYoga can add answer to question`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            get("/questionnaires/2/edit/question/1/addAnswer")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question1") { exists() }
                node("#question1Header") { exists() }
                node("#question1Body") { exists() }

                node("#answer1") { exists() }
                node(".answer0Id") { exists() }
                node(".answer0QuestionId") { exists() }
                node(".answer0Score") { exists() }
                node("#answer2") { exists() }
                node(".answer1Id") { exists() }
                node(".answer1QuestionId") { exists() }
                node(".answer1Score") { exists() }
            }
        }
    }*/

    /*@Test
    fun `QYoga can delete answer to question`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            delete("/questionnaires/2/edit/answer/1")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#question1") { exists() }
                node("#question1Header") { exists() }
                node("#question1Body") { exists() }

                node("#answer1") { notExists() }
                node(".answer0Id") { notExists() }
                node(".answer0QuestionId") { notExists() }
                node(".answer0Score") { notExists() }
            }
        }
    }*/

    @Test
    fun `QYoga can update questionnaire title`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit")
            param("id", 1)
            param("title", "asd")
            post("/therapist/questionnaires/edit/title")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }

    @Test
    fun `QYoga can get questionnaire edit page`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/edit")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node("#questions") { exists() }
                node("#question0") { exists() }
                node("#question0Header") { exists() }
                node("#question0Body") { exists() }
                node("#answer0") { exists() }
            }
        }
    }

    /*@Test
    fun `QYoga can update answer`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            param("id", "1")
            param("title", "test")
            param("questions[0].id", "1")
            param("questions[0].questionnaireId", "1")
            param("questions[0].title", "questions title")
            param("questions[0].questionType", "SEVERAL")
            param("questions[0].answers[0].id", "1")
            param("questions[0].answers[0].questionId", "1")
            param("questions[0].answers[0].score", "1")
            param("questions[0].answers[0].title", "answer title")
            post("/questionnaires/question/1/answer/1/update")
        } Then {
            extract().statusCode().compareTo(200) shouldBe 0
        }
    }*/

    /*@Test
    fun `QYoga can change answer fragment to set answer score`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            param("questionIndex", 0)
            param("id", "1")
            param("title", "test")
            param("questions[0].id", "1")
            param("questions[0].questionnaireId", "1")
            param("questions[0].title", "questions title")
            param("questions[0].questionType", "SINGLE")
            param("questions[0].answers[0].id", "1")
            param("questions[0].answers[0].questionId", "1")
            param("questions[0].answers[0].score", "1")
            param("questions[0].answers[0].title", "answer title")
            post("/questionnaires/question/1/setScores")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".answerScore") { exists() }
                node(".saveScore") { exists() }
            }
        }
    }*/

    /*@Test
    fun `QYoga can change answer fragment to change answer fields after set scores`() {
        Given {
            authorized()
        } When {
            get("/questionnaires/new")
            param("questionIndex", 0)
            param("id", "1")
            param("title", "test")
            param("questions[0].id", "1")
            param("questions[0].questionnaireId", "1")
            param("questions[0].title", "questions title")
            param("questions[0].questionType", "SINGLE")
            param("questions[0].answers[0].id", "1")
            param("questions[0].answers[0].questionId", "1")
            param("questions[0].answers[0].score", "1")
            param("questions[0].answers[0].title", "answer title")
            post("/questionnaires/question/1/setAnswers")
        } Then {
            val body = Jsoup.parse(extract().body().asString())
            io.github.ulfs.assertj.jsoup.Assertions.assertThatSpec(body) {
                node(".typeSINGLE") { hasAttribute("selected") }
                node(".setScore") { exists() }
            }
        }
    }*/

    @Test
    fun `QYoga can save question`() {
        Given {
            authorized()
        } When {
            get("/therapist/questionnaires/new")
            get("/therapist/questionnaires/edit")
            param("id", "1")
            param("title", "test")
            param("questions[0].id", "1")
            param("questions[0].questionnaireId", "1")
            param("questions[0].title", "questions title")
            param("questions[0].questionType", "SINGLE")
            param("questions[0].answers[0].id", "1")
            param("questions[0].answers[0].questionId", "1")
            param("questions[0].answers[0].score", "1")
            param("questions[0].answers[0].title", "answer title")
            post("/therapist/questionnaires/edit")
        } Then {
            extract().statusCode().compareTo(302) shouldBe 0
        }
    }
}
