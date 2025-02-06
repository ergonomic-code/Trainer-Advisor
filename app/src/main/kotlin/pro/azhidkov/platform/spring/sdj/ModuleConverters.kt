package pro.azhidkov.platform.spring.sdj

import org.springframework.core.convert.converter.Converter

fun interface ModuleConverters {

    fun converters(): Set<Converter<*, *>>

}