package pro.qyoga.core.clients.therapeutic_data.descriptors

import org.springframework.data.relational.core.mapping.MappedCollection
import org.springframework.data.relational.core.mapping.Table
import pro.azhidkov.platform.extensible_entity.descriptor.ExtensibleEntity
import pro.azhidkov.platform.kotlin.mapElement
import pro.qyoga.core.users.therapists.TherapistRef
import java.time.Instant


@Table("therapeutic_data_descriptors")
data class TherapeuticDataDescriptor(
    val ownerRef: TherapistRef,

    @MappedCollection(idColumn = "therapeutic_data_descriptor_id", keyColumn = "block_index")
    override val blocks: List<TherapeuticDataBlock>,

    override val id: Long = 0,
    override val createdAt: Instant = Instant.now(),
    override val modifiedAt: Instant? = null,
    override val version: Long = 0
) : ExtensibleEntity<TherapeuticDataField, TherapeuticDataBlock>() {

    fun withBlock(addedBlock: TherapeuticDataBlock): TherapeuticDataDescriptor =
        this.copy(blocks = blocks + addedBlock)

    fun withBlockPatchedBy(
        blockIdx: Int,
        modifyBlock: (TherapeuticDataBlock) -> TherapeuticDataBlock
    ): TherapeuticDataDescriptor =
        this.copy(blocks = blocks.mapElement(blockIdx) { modifyBlock(it) })

    fun withFieldPatchedBy(
        blockIdx: Int,
        fieldIdx: Int,
        modifyField: (TherapeuticDataField) -> TherapeuticDataField
    ): TherapeuticDataDescriptor =
        this.copy(blocks = blocks.mapElement(blockIdx) { it.withFieldPatchedBy(fieldIdx, modifyField) })

    fun withOwner(newOwnerRef: TherapistRef): TherapeuticDataDescriptor =
        this.copy(ownerRef = newOwnerRef)

}