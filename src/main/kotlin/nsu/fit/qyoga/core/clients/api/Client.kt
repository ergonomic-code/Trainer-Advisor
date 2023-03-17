package nsu.fit.qyoga.core.clients.api

import jakarta.validation.constraints.Email
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.util.*

@Table("clients")
data class Client(
    @Id
    val id: Long,
    val name: String,
    val secondname: String,
    val surname: String,
    val birthdate: Date,
    val email: Email,
    val phoneNumber: Long,
    val address: String,
    val distributionSourse: String,
    val workingDignose: String
)
