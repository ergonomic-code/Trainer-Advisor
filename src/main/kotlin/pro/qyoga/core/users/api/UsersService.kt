package pro.qyoga.core.users.api


interface UsersService {

    fun registerNewTherapist(registerTherapistRequest: RegisterTherapistRequest, password: String): Therapist?

}