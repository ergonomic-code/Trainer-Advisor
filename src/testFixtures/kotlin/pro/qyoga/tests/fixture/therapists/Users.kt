package pro.qyoga.tests.fixture.therapists

import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.security.core.authority.SimpleGrantedAuthority
import pro.qyoga.core.users.api.Role
import pro.qyoga.core.users.api.Therapist
import pro.qyoga.core.users.internal.QyogaUserDetails


const val THE_THERAPIST_LOGIN = "therapist@qyoga.pro"
const val THE_THERAPIST_PASSWORD = "password"
const val THE_THERAPIST_ID = 1L
val THE_THERAPIST_REF = AggregateReference.to<Therapist, Long>(1L)

val theTherapistUserDetails = QyogaUserDetails(
    THE_THERAPIST_ID,
    THE_THERAPIST_LOGIN,
    THE_THERAPIST_PASSWORD,
    setOf(SimpleGrantedAuthority(Role.ROLE_THERAPIST.name))
)