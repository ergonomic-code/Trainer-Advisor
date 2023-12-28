package pro.qyoga.tests.fixture.therapists

import org.springframework.security.core.authority.SimpleGrantedAuthority
import pro.qyoga.core.users.api.Role
import pro.qyoga.core.users.internal.QyogaUserDetails

val theTherapistUserDetails = QyogaUserDetails(
    THE_THERAPIST_ID,
    THE_THERAPIST_LOGIN,
    THE_THERAPIST_PASSWORD,
    setOf(SimpleGrantedAuthority(Role.ROLE_THERAPIST.name))
)

fun idOnlyUserDetails(id: Long) = QyogaUserDetails(id, "", "", emptySet())