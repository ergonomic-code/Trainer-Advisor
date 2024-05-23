package pro.azhidkov.platform.extensible_entity.values

import pro.azhidkov.platform.extensible_entity.descriptor.CustomField


sealed class CustomFieldValue {

    abstract val field: CustomField
    abstract val value: Any?

}

class StringFieldValue(
    override val field: CustomField,
    override val value: String?,
) : CustomFieldValue()

class IntFieldValue(
    override val field: CustomField,
    override val value: Int?,
) : CustomFieldValue()

class DoubleFieldValue(
    override val field: CustomField,
    override val value: Double?,
) : CustomFieldValue()

class BooleanFieldValue(
    override val field: CustomField,
    override val value: Boolean?,
) : CustomFieldValue()