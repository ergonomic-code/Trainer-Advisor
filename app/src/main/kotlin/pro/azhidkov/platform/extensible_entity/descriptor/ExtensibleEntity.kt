package pro.azhidkov.platform.extensible_entity.descriptor

import org.springframework.data.annotation.*
import java.time.Instant


abstract class ExtensibleEntity<FIELD : CustomField, BLOCK : CustomFieldBlock<FIELD>> {

    abstract val blocks: List<BLOCK>

    @get:Id
    abstract val id: Long

    @get:CreatedDate
    abstract val createdAt: Instant

    @get:LastModifiedDate
    abstract val modifiedAt: Instant?

    @get:Version
    abstract val version: Long

    @get:Transient
    val fields: List<FIELD>
        get() = blocks.flatMap { it.fields }

    operator fun get(idx: Int): BLOCK =
        blocks[idx]

}