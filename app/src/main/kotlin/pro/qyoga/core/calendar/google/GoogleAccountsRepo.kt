package pro.qyoga.core.calendar.google

import pro.qyoga.core.users.therapists.TherapistRef


class GoogleAccountsRepo {

    private val repo = HashMap<TherapistRef, List<GoogleAccount>>()

    fun addGoogleAccount(therapist: TherapistRef, googleAccount: GoogleAccount) {
        repo[therapist] = repo.getOrDefault(therapist, emptyList()) + googleAccount
    }

    fun findGoogleAccounts(therapist: TherapistRef): List<GoogleAccount> {
        return repo[therapist] ?: emptyList()
    }

}