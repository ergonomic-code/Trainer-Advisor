package pro.azhidkov.platform.errors

import kotlin.reflect.KProperty1
import kotlin.reflect.jvm.jvmErasure


class ResourceNotFoundException(
    val type: String,
    val keys: List<Pair<String, Any?>>,
    override val message: String = "Resource $type not found by ${keys.format()}",
    errorCode: String = "resource-not-found",
    cause: Throwable? = null
) : DomainError(message, cause, errorCode = errorCode) {

    init {
        require(keys.isNotEmpty())
    }

    constructor(prop: KProperty1<*, Any?>, key: Any) : this(
        prop.parameters[0].type.jvmErasure.simpleName!!,
        listOf(prop.name to key)
    )

}

private fun List<Pair<String, Any?>>.format(): String =
    when (size) {
        1 -> first().let(::formatAttrValue)
        else -> this.joinToString(", ", prefix = "[", postfix = "]", transform = ::formatAttrValue)
    }

private fun formatAttrValue(attrValue: Pair<String, Any?>): String = "${attrValue.first}=${attrValue.second}"