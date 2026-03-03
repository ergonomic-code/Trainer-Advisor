package pro.azhidkov.platform.spring.sdj.converters

import com.fasterxml.jackson.databind.ObjectMapper
import org.postgresql.util.PGobject
import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import kotlin.reflect.KClass


@WritingConverter
fun interface ObjectToJsonbWriter<T : Any> : Converter<T, PGobject>

@ReadingConverter
fun interface JsonbToObjectReader<T> : Converter<PGobject, T?>

abstract class JacksonObjectToJsonbWriter<T : Any>(
    private val objectMapper: ObjectMapper
) : ObjectToJsonbWriter<T> {

    override fun convert(source: T): PGobject =
        source.let {
            PGobject().apply {
                type = "jsonb"
                value = objectMapper.writeValueAsString(source)
            }
        }

}

@ReadingConverter
abstract class JacksonJsonbToObjectReader<T : Any>(
    private val objectMapper: ObjectMapper,
    private val type: KClass<T>
) : JsonbToObjectReader<T> {

    override fun convert(source: PGobject): T? =
        if (source.isNull) {
            null
        } else {
            objectMapper.readValue(source.value!!, type.java)
        }

}

object ObjectToJsonbConverters {

    inline fun <reified T : Any> convertersFor(objectMapper: ObjectMapper) =
        setOf(
            object : JacksonObjectToJsonbWriter<T>(objectMapper) {},
            object : JacksonJsonbToObjectReader<T>(objectMapper, T::class) {},
        )

}
