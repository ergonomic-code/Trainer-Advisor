package pro.azhidkov.platform.spring.sdj.converters

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter

data class SecretChars(val value: CharArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretChars

        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }
}

@WritingConverter
class SecretCharsToString : Converter<SecretChars, String> {
    override fun convert(source: SecretChars) = String(source.value)
}

@ReadingConverter
class StringToSecretChars : Converter<String, SecretChars> {
    override fun convert(source: String) = SecretChars(source.toCharArray())
}