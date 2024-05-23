package pro.qyoga.tests.cases.core.clients.therapeutic_data.descriptors

import io.kotest.matchers.collections.shouldMatchInOrder
import io.kotest.matchers.shouldBe
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptorsRepo
import pro.qyoga.tests.assertions.shouldMatch
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother.therapeuticDataBlock
import pro.qyoga.tests.fixture.object_mothers.clients.TherapeuticDataDescriptorsObjectMother.therapeuticDataDescriptor
import pro.qyoga.tests.infra.test_config.spring.context
import pro.qyoga.tests.infra.web.QYogaAppIntegrationBaseTest


@DisplayName("Репозиторий дескрипторов терапевтических данных")
class TherapeuticDataDescriptorsRepoTest : QYogaAppIntegrationBaseTest() {

    private val therapeuticDataDescriptorsRepo = context.getBean(TherapeuticDataDescriptorsRepo::class.java)

    @DisplayName("Идентификатор пользовательского поля должен оставаться неизменным при обновлении агрегата")
    @Test
    fun testFieldIdPersistence() {
        // Given
        val therapeuticDataDescriptor = backgrounds.clients.createTherapeuticDataDescriptor {
            therapeuticDataDescriptor(blocksCount = 1, fieldsPerBlock = 1)
        }
        val updatedTherapeutic = therapeuticDataDescriptor.withFieldPatchedBy(blockIdx = 0, fieldIdx = 0) {
            it.withLabel(randomCyrillicWord())
        }

        // When
        therapeuticDataDescriptorsRepo.save(updatedTherapeutic)
        val loadedTherapeuticData = therapeuticDataDescriptorsRepo.findByIdOrNull(updatedTherapeutic.id)!!

        // Then
        loadedTherapeuticData.fields.single() shouldBe updatedTherapeutic.fields.single()
    }

    @DisplayName("При добавлении 2+ блока метод save должен возвращать тот же список блоков, что был подан на вход")
    @Test
    fun onAddBlockSaveShouldReturnAggregateWithTheSameBlocks() {
        // Given
        val therapeuticDataDescriptorWithSingleBlock = backgrounds.clients.createTherapeuticDataDescriptor {
            therapeuticDataDescriptor(blocksCount = 1)
        }

        val addedBlock = therapeuticDataBlock()
        val therapeuticDataDescriptorWithAddedBlock = therapeuticDataDescriptorWithSingleBlock.withBlock(addedBlock)

        // When
        val returnedTherapeuticDataDescriptor =
            therapeuticDataDescriptorsRepo.save(therapeuticDataDescriptorWithAddedBlock)

        // Then
        returnedTherapeuticDataDescriptor.blocks shouldMatch therapeuticDataDescriptorWithAddedBlock.blocks
    }

    @DisplayName("При добавлении 2+ поля метод save должен возвращать тот же список полей, что был подан на вход")
    @Test
    fun onAddFieldSaveShouldReturnAggregateWithTheSameBlocks() {
        // Given
        val therapeuticDataDescriptorWithSingleField = backgrounds.clients.createTherapeuticDataDescriptor {
            therapeuticDataDescriptor(blocksCount = 1, fieldsPerBlock = 1)
        }

        val addedField = TherapeuticDataDescriptorsObjectMother.therapeuticDataField()
        val therapeuticDataDescriptorWithAddedField =
            therapeuticDataDescriptorWithSingleField.withBlockPatchedBy(0) { it.withField(addedField) }

        // When
        val returnedTherapeuticDataDescriptor =
            therapeuticDataDescriptorsRepo.save(therapeuticDataDescriptorWithAddedField)

        // Then
        returnedTherapeuticDataDescriptor.fields shouldMatchInOrder therapeuticDataDescriptorWithAddedField.fields.map { expected ->
            { actual -> actual shouldMatch expected }
        }
    }

}
