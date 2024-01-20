package pro.qyoga.tests.fixture.data

import kotlin.random.Random


val documentExtensions = listOf("docx", "xlsx", "pptx", "pdf", "txt")

val imageExtensions = listOf("png", "jpeg", "gif")

val allFilesExtensions = documentExtensions + imageExtensions

fun randomImageExtension() = imageExtensions.random()

fun randomFileExtension() = allFilesExtensions.random()

fun randomFileBaseName() = randomLatinWord(3, 9)

fun randomTestFileSize() = Random.nextInt(1, 256)

fun fileName(baseName: String, extension: String) = "$baseName.$extension"

