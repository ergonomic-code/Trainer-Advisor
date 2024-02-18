package pro.qyoga.tests.fixture.object_mothers.therapists

import org.springframework.security.core.authority.SimpleGrantedAuthority
import pro.qyoga.core.users.auth.dtos.QyogaUserDetails
import pro.qyoga.core.users.auth.model.Role

val theTherapistUserDetails = QyogaUserDetails(
    THE_THERAPIST_ID,
    THE_THERAPIST_LOGIN,
    THE_THERAPIST_PASSWORD,
    setOf(SimpleGrantedAuthority(Role.ROLE_THERAPIST.name))
)

fun idOnlyUserDetails(id: Long) = QyogaUserDetails(id, "", "", emptySet())