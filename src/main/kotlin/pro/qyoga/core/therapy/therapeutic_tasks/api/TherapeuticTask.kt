package pro.qyoga.core.therapy.therapeutic_tasks.api

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.annotation.Version
import org.springframework.data.jdbc.core.mapping.AggregateReference
import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.users.api.Therapist
import pro.qyoga.platform.spring.sdj.erpo.hydration.Identifiable
import java.time.Instant

typealias TherapeuticTaskRef = AggregateReference<TherapeuticTask, Long>

@Table("therapeutic_tasks")
data class TherapeuticTask(
    val owner: AggregateReference<Therapist, Long>,
    val name: String,

    @Id
    override val id: Long = 0,
    @CreatedDate
    val createdAt: Instant = Instant.now(),
    @LastModifiedDate
    val lastModifiedAt: Instant? = null,
    @Version
    val version: Long = 0,
) : Identifiable<Long> {

    constructor(owner: Long, taskName: String) : this(AggregateReference.to(owner), taskName)

    fun withName(newName: String): TherapeuticTask {
        return copy(name = newName)
    }

}
