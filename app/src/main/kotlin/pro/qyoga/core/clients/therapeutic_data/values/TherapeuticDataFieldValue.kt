package pro.qyoga.core.clients.therapeutic_data.values

import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataFieldRef


@Table("therapeutic_data_field_values")
sealed interface TherapeuticDataFieldValue<T> {
    val clientRef: ClientRef
    val fieldRef: TherapeuticDataFieldRef
    val value: T
}