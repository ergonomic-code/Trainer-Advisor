package pro.azhidkov.platform.extensible_entity.descriptor


abstract class CustomFieldBlock<T : CustomField> {

    abstract val label: String
    abstract val fields: List<T>

}