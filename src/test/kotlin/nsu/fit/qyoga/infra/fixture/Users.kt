package nsu.fit.qyoga.infra.fixture

import nsu.fit.qyoga.core.users.LoginRequest

val adminLoginRequest = LoginRequest("admin", "diem-Synergy5")

val adminWithInvalidPasswordLoginRequest = adminLoginRequest.copy(password = "invalid")

val notExistingUserLoginRequest = LoginRequest("not-existing", "any")

val adminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIl0sInN1YiI6IjEiLCJpYXQiOjE2NjYxNDg1MDR9.nE37BqZhBidqR2C_voONJjJAMmrpt-_JrHeWrNlLat0"

// sub: 1 -> 2
val editedAdminToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX0FETUlOIl0sInN1YiI6IjIiLCJpYXQiOjE2NjYxNDg1MDR9.nE37BqZhBidqR2C_voONJjJAMmrpt-_JrHeWrNlLat0"

val brokenAdminToken = "YiI6IjIiLCJpYXQiOjE2NjYxNDg1MDR9.nE37BqZhBidqR2C_voONJjJAMmrpt-_JrHeWrNlLat0"
