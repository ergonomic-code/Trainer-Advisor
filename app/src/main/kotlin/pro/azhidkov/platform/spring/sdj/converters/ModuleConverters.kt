package pro.azhidkov.platform.spring.sdj.converters

import org.springframework.core.convert.converter.Converter

fun interface ModuleConverters {

    fun converters(): Set<Converter<*, *>>

}