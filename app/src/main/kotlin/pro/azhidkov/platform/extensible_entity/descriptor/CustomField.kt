package pro.azhidkov.platform.extensible_entity.descriptor

import org.springframework.data.annotation.Id


abstract class CustomField {
    abstract val label: String
    abstract val type: CustomFieldType
    abstract val required: Boolean

    @get:Id
    abstract val id: Long
}