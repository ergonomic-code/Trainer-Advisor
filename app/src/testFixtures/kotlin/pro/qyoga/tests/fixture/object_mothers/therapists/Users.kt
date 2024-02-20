package pro.qyoga.tests.fixture.object_mothers.therapists

import org.springframework.data.jdbc.core.mapping.AggregateReference
import pro.qyoga.core.users.therapists.Therapist

// Therapist
const val THE_THERAPIST_ID = 1L

const val THE_THERAPIST_LOGIN = "therapist@qyoga.pro"
const val THE_THERAPIST_PASSWORD = "password"

const val THE_THERAPIST_FIRST_NAME = "Елена (тест)"

val THE_THERAPIST_REF = AggregateReference.to<Therapist, Long>(THE_THERAPIST_ID)

// Admin
const val THE_ADMIN_ID = 2L

const val THE_ADMIN_LOGIN = "admin@ta.pro"
const val THE_ADMIN_PASSWORD = "password"

const val THE_ADMIN_FIRST_NAME = "Админ"

val THE_ADMIN_REF = AggregateReference.to<Therapist, Long>(THE_ADMIN_ID)
