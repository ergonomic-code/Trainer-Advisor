package nsu.fit.qyoga.core.completingQuestionnaires.api.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.util.*

@Table("completing")
data class Completing(
    @Id
    val id: Long = 0,
    val questionnaireId: Long,
    val completingDate: LocalDate,
    val clientId: Long
)
