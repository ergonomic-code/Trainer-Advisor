package pro.qyoga.core.clients.cards.dtos

data class ClientSearchDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val phoneNumber: String? = null,
) {

    companion object {

        val ALL = ClientSearchDto()

    }

}