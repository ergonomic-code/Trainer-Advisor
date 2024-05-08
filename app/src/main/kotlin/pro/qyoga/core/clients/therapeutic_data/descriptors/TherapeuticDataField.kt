package pro.qyoga.core.clients.therapeutic_data.descriptors

import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.extensible_entity.descriptor.CustomField
import pro.azhidkov.platform.extensible_entity.descriptor.CustomFieldType


@Table("therapeutic_data_fields")
data class TherapeuticDataField(
    override val label: String,
    override val type: CustomFieldType,
    override val required: Boolean,

    override val id: Long = 0
) : CustomField() {

    fun withLabel(newLabel: String) =
        this.copy(label = newLabel)

}