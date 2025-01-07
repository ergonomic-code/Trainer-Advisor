package pro.qyoga.tests.fixture.object_mothers.therapists

import pro.qyoga.core.users.therapists.Therapist
import pro.qyoga.core.users.therapists.TherapistRef
import java.util.*

// Therapist
val THE_THERAPIST_ID: UUID = UUID.fromString("d43cf6b1-8b8c-4b45-b48c-b350dd99e497")

const val THE_THERAPIST_LOGIN = "therapist@qyoga.pro"
const val THE_THERAPIST_PASSWORD = "password"

const val THE_THERAPIST_FIRST_NAME = "Елена (тест)"

val THE_THERAPIST_REF = TherapistRef.to<Therapist, UUID>(THE_THERAPIST_ID)

// Admin
const val THE_ADMIN_LOGIN = "admin@ta.pro"
const val THE_ADMIN_PASSWORD = "password"
