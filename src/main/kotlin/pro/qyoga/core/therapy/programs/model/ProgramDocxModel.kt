package pro.qyoga.core.therapy.programs.model

data class DocxProgram(
    val id: Long,
    val title: String,
    val exercises: List<DocxExercise>
)

data class DocxExercise(
    val id: Long,
    val title: String,
    val description: String,
    val steps: List<DocxStep>
)

data class DocxStep(
    val idx: Int,
    val description: String,
    val imageId: Long?
)