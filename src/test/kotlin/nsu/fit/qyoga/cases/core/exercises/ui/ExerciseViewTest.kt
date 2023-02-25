package nsu.fit.qyoga.cases.core.exercises.ui

import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseDto
import nsu.fit.qyoga.core.exercises.api.dtos.ExerciseSearchDto
import nsu.fit.qyoga.core.exercises.api.model.ExerciseType
import nsu.fit.qyoga.infra.QYogaAppTestBase
import org.apache.http.HttpStatus
import org.junit.jupiter.api.Test
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

class ExerciseViewTest : QYogaAppTestBase() {

    @Test
    fun `QYoga should return initial page`() {
        When {
            get("/exercises/all")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
            status().is3xxRedirection
        }
    }

    @Test
    fun `QYoga should return pagination view with default params`() {
        When {
            get("/exercises/")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
            status().is3xxRedirection
        }
    }

    @Test
    fun `QYoga should return pagination view using params`() {
        Given {
            param("title", "")
            param("contradiction", "")
            param("duration", "")
            param("exerciseType", "")
            param("therapeuticPurpose", "")
            param("pageSize", 20)
            param("pageNumber", 1)
        } When {
            get("/exercises/")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
            status().is3xxRedirection
        }
    }

    @Test
    fun `QYoga should return filtered view for collection with default params`() {
        When {
            get("/exercises/search")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
        }
    }

    @Test
    fun `QYoga should return filtered view for collection using params`() {
        Given {
            param("searchDto", createSearchDto())
            param("pageSize", 20)
            param("pageNumber", 1)
        } When {
            get("/exercises/search")
        } Then {
            statusCode(HttpStatus.SC_OK)
            view().name("ExercisesSearch")
            model().attributeExists("searchDto")
            model().attributeExists("exercises")
            model().attributeExists("types")
            model().attributeExists("pageNumbers")
            model().attribute("exercises.content", listOf(ExerciseDto(1, "", "", "", "", "", "", "")))
        }
    }

    fun createSearchDto() = ExerciseSearchDto("", "", "", ExerciseType.Balance, "")
}
