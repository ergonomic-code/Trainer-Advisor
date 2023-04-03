package nsu.fit.qyoga.core.programs.api

import org.springframework.data.annotation.Id
import java.time.Instant

data class Program(
    @Id
    val id: Long = 0,
    val title: String,
    val date: Instant
)