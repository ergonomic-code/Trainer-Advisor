package pro.qyoga.tests.fixture.data

val lowerCaseCyrillicLetters = ('а'..'я').toList()
val upperCaseCyrillicLetters = ('А'..'Я').toList()
val cyrillicLetters = lowerCaseCyrillicLetters + upperCaseCyrillicLetters

val lowerCaseLatinLetters = ('a'..'z').toList()

fun randomWord(letters: List<Char>, minLength: Int = 1, maxLength: Int = 12) =
    buildString {
        val length = faker.random().nextInt(minLength, maxLength)
        repeat(length) {
            append(letters[faker.random().nextInt(letters.size)])
        }
        check(this.length in minLength..maxLength)
    }

fun randomCyrillicWord(minLength: Int = 1, maxLength: Int = 12) =
    randomWord(cyrillicLetters, minLength, maxLength)

fun randomLatinWord(minLength: Int = 1, maxLength: Int = 12) =
    randomWord(lowerCaseLatinLetters, minLength, maxLength)

fun randomSentence(minWords: Int = 3, maxWords: Int = 20): String {
    val length = faker.random().nextInt(minWords, maxWords)
    val sentence = (1..length).joinToString(". ") {
        randomCyrillicWord(minLength = 3)
    }
    check(sentence.isNotEmpty())
    return sentence
}

fun randomEmail(): String =
    buildString {
        append(randomWord(lowerCaseLatinLetters, 4, 8))
        append("@")
        append(randomWord(lowerCaseLatinLetters, 4, 8))
        append(".")
        append(randomWord(lowerCaseLatinLetters, 2, 3))
    }

fun randomPassword() = randomLatinWord(minLength = 8)