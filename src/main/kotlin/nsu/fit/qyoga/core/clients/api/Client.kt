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
    val phone_number: Long,
    val address: String,
    val distribution_sourse: String,
    val working_dignose: String
)
