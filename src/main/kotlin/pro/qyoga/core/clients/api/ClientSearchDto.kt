package pro.qyoga.core.clients.api

data class ClientSearchDto(
    val firstName: String? = null,
    val lastName: String? = null,
    val patronymic: String? = null,
    val phoneNumber: String? = null,
) {

    companion object {

        val ALL = ClientSearchDto()

    }

}
