package nsu.fit.qyoga.core.completingQuestionnaires.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

@Table("completing")
data class Completing(
    @Id
    val id: Long = 0,
    val completingDate: LocalDate?,
    val numericResult: Long,
    val textResult: String,
    val questionnaireId: Long,
    val clientId: Long,
    val therapistId: Long
)
