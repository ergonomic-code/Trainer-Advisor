package pro.qyoga.tests.fixture.object_mothers.therapy.exercises

import pro.azhidkov.platform.file_storage.api.StoredFile
import pro.qyoga.tests.fixture.object_mothers.FilesObjectMother


sealed interface ImagesGenerationMode {

    fun generateImages(steps: Int): Map<Int, StoredFile>

}

data object AllSteps : ImagesGenerationMode {

    override fun generateImages(steps: Int): Map<Int, StoredFile> =
        (1..steps).associateWith { FilesObjectMother.randomImage() }

}

data object None : ImagesGenerationMode {

    override fun generateImages(steps: Int): Map<Int, StoredFile> = emptyMap()

}