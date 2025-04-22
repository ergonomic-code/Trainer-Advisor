package pro.qyoga.core.clients.cards.model


interface HasPersonName {

    val firstName: String
    val lastName: String
    val middleName: String?

    fun fullName() = listOf(lastName, firstName, middleName)
        .filter { it?.isNotBlank() ?: false }
        .joinToString(" ")

}