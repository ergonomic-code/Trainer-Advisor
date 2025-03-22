package pro.qyoga.core.clients.cards.dtos

data class ClientSearchDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
) {

    val digitsOnlyPhoneNumber = phoneNumber?.replace("[^0-9]".toRegex(), "")

    companion object {

        val ALL = ClientSearchDto()

    }

}