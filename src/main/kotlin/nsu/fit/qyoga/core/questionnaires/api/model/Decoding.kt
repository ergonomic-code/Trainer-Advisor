package nsu.fit.qyoga.core.questionnaires.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("decoding")
data class Decoding(
    @Id
    val id: Long = 0,
    val lowerBound: Int,
    val upperBound: Int,
    val result: String,
    val questionnaireId: Long
)
