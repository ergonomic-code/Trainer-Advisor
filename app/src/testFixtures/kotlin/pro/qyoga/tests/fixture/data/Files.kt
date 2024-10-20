package pro.qyoga.tests.fixture.data


val documentExtensions = listOf("docx", "xlsx", "pptx", "pdf", "txt")

val imageExtensions = listOf("png", "jpeg", "gif")

val allFilesExtensions = documentExtensions + imageExtensions

fun randomImageExtension() = imageExtensions.randomElement()

fun randomFileExtension() = allFilesExtensions.randomElement()

fun randomFileBaseName() = randomLatinWord(3, 9)

fun randomTestFileSize(): Int = faker.random().nextInt(1, 256)

fun fileName(baseName: String, extension: String) = "$baseName.$extension"

