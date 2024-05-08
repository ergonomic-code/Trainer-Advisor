package pro.qyoga.core.clients.therapeutic_data.descriptors

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.extensible_entity.descriptor.CustomFieldBlock
import pro.azhidkov.platform.kotlin.mapElement


@Table("therapeutic_data_blocks")
data class TherapeuticDataBlock(
    override val label: String,

    @MappedCollection(idColumn = "therapeutic_data_block_id", keyColumn = "field_index")
    override val fields: List<TherapeuticDataField>,

    @Id
    val id: Long = 0
) : CustomFieldBlock<TherapeuticDataField>() {

    init {
        check(fields.isNotEmpty()) { "Block should have at least one field" }
    }

    fun withField(addedField: TherapeuticDataField): TherapeuticDataBlock =
        this.copy(fields = fields + addedField)

    fun withFieldPatchedBy(
        fieldIdx: Int,
        modifyField: (TherapeuticDataField) -> TherapeuticDataField
    ): TherapeuticDataBlock =
        this.copy(fields = fields.mapElement(fieldIdx) { modifyField(it) })

}