package nsu.fit.qyoga.core.therapeutic_purposes.api

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("therapeutic_purposes")
data class TherapeuticPurpose(
    val purpose: String,
    @Id
    val id: Long = 0
)
