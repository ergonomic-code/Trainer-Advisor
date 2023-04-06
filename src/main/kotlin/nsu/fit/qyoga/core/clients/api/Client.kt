package nsu.fit.qyoga.core.clients.api

import jakarta.validation.constraints.Email
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*


@Table("clients")
data class Client(
    @Id
    val id: Long,
    val firstname: String,
    val patronymic: String,
    val lastname: String,
    val birthdate: Date,
    @Email
    val email: String,
    val phoneNumber: Long,
    val address: String,
    val distributionSourse: String,
    val workingDignose: String
)
