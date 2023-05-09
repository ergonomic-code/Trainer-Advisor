package nsu.fit.qyoga.cases.core.therapeutic_purposes.internal

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import nsu.fit.qyoga.cases.core.therapeutic_purposes.TherapeuticPurposesTestConfig
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurpose
import nsu.fit.qyoga.core.therapeutic_purposes.api.TherapeuticPurposesService
import nsu.fit.qyoga.infra.TestContainerDbContextInitializer
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration


@ContextConfiguration(
    classes = [TherapeuticPurposesTestConfig::class],
    initializers = [TestContainerDbContextInitializer::class]
)
@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.NONE
)
@ActiveProfiles("test")
class TherapeuticPurposesServiceTest {

    @Autowired
    private lateinit var therapeuticPurposesService: TherapeuticPurposesService

    @Test
    fun `TherapeuticPurposesService should return object with updated id after saving`() {

        // When
        val existing = therapeuticPurposesService.save(TherapeuticPurpose("Irrelevant"))

        // Then
        existing.id shouldNotBe 0
    }

    @Test
    fun `When therapeutic purpose with existing name is saved, than service should return TP with id of existing one`() {
        // Given
        val name = "Вытяжение шеи"
        val originalTherapeuticPurpose = therapeuticPurposesService.save(TherapeuticPurpose(name))

        // When
        val existing = therapeuticPurposesService.save(TherapeuticPurpose(name))

        // Then
        existing.id shouldBe originalTherapeuticPurpose.id
    }

}