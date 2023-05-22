package nsu.fit.qyoga.core.therapists.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("therapists")
data class Therapist(
    @Id
    val id: Long = 0,
    val name: String
)
