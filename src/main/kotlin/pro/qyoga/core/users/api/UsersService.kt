package pro.qyoga.core.users.api


interface UsersService {

    fun registerNewTherapist(registerTherapistRequest: RegisterTherapistRequest): Therapist?

}