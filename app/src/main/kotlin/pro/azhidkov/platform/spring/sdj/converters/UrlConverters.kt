package pro.azhidkov.platform.spring.sdj.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import java.net.URI
import java.net.URL

@WritingConverter
class URLToStringConverter : Converter<URL, String> {
    override fun convert(source: URL): String {
        return source.toExternalForm()
    }
}

@ReadingConverter
class StringToURLConverter : Converter<String, URL> {
    override fun convert(source: String): URL {
        return URI.create(source).toURL()
    }
}