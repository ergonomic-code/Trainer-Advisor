package pro.qyoga.core.clients.therapeutic_data.values

import org.springframework.data.relational.core.mapping.Table
import pro.qyoga.core.clients.cards.model.ClientRef
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataFieldRef

data class StringTherapeuticDataFieldValue(
    override val clientRef: ClientRef,
    override val fieldRef: TherapeuticDataFieldRef,
    override val value: String,
) : TherapeuticDataFieldValue<String>