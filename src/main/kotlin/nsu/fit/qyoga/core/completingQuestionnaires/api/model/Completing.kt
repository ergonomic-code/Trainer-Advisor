package nsu.fit.qyoga.core.completingQuestionnaires.api.model

import org.springframework.data.annotation.Id
import java.util.*

data class Completing(
    @Id
    val id: Long = 0,
    val questionnaireId: Long,
    val completingDate: Date,
)
