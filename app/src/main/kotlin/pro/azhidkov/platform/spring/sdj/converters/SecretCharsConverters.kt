package pro.azhidkov.platform.spring.sdj.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter
import pro.azhidkov.platform.secrets.SecretChars

@WritingConverter
class SecretCharsToString : Converter<SecretChars, String> {
    override fun convert(source: SecretChars) = source.show()
}

@ReadingConverter
class StringToSecretChars : Converter<String, SecretChars> {
    override fun convert(source: String) = SecretChars(source.toCharArray())
}
