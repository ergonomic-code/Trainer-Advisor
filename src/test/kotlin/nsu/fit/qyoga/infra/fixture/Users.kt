package nsu.fit.qyoga.infra.fixture

import nsu.fit.qyoga.core.users.api.LoginRequest

val adminLoginRequest = LoginRequest("admin", "diem-Synergy5")

val adminWithInvalidPasswordLoginRequest = adminLoginRequest.copy(password = "invalid")

val notExistingUserLoginRequest = LoginRequest("not-existing", "any")

const val EDITED_ADMIN_TOKEN =
    "eyJhbGciOiJIUzI1NiJ9." + "eyJyb2xlcyI6WyJST0xFX0FETUlOIl0sInN1YiI6IjIiLCJpYXQiOjE2NjYxNDg1MDR9" + ".nE37BqZhBidqR2C_voONJjJAMmrpt-_JrHeWrNlLat0"

const val BROKEN_ADMIN_TOKEN = "YiI6IjIiLCJpYXQiOjE2NjYxNDg1MDR9.nE37BqZhBidqR2C_voONJjJAMmrpt-_JrHeWrNlLat0"
