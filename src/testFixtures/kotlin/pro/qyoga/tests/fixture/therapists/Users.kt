package pro.qyoga.tests.fixture.therapists

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.users.api.Therapist


const val THE_THERAPIST_LOGIN = "therapist@qyoga.pro"
const val THE_THERAPIST_PASSWORD = "password"
const val THE_THERAPIST_ID = 1L
val THE_THERAPIST_REF = AggregateReference.to<Therapist, Long>(1L)
