package pro.qyoga.tests.fixture.data

val imageExtensions = listOf("png", "jpeg", "gif")

fun randomImageExtension() = imageExtensions.random()

fun randomFileBaseName() = randomLatinWord(3, 9)

fun fileName(baseName: String, extension: String) = "$baseName.$extension"

