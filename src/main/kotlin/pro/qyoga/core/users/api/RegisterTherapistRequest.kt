package pro.qyoga.core.users.api


data class RegisterTherapistRequest(
    val firstName: String,
    val lastName: String,
    val email: String
) {

    val fullName: String = "$firstName $lastName"

}
