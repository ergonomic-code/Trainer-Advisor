package pro.qyoga.core.clients.cards.model

import org.springframework.core.convert.converter.Converter
import org.springframework.data.convert.ReadingConverter
import org.springframework.data.convert.WritingConverter


data class PhoneNumber(
    val countryCode: String,
    val nationalNumber: String
) {

    init {
        require(countryCode.length <= 4)
        require(countryCode.startsWith("+"))
        require(nationalNumber.length == 10)
    }

    companion object {

        fun of(phone: String): PhoneNumber {
            val digitsOnly = phone.replace("[^0-9]".toRegex(), "")
            return PhoneNumber(
                "+" + digitsOnly.take(digitsOnly.length - 10),
                digitsOnly.takeLast(10)
            )
        }

    }

}

fun PhoneNumber.toE164Format() = "$countryCode$nationalNumber"

fun PhoneNumber.toUIFormat() = "$countryCode (${nationalNumber.take(3)}) ${nationalNumber.substring(3..5)}-${
    nationalNumber.substring(6..7)
}-${nationalNumber.substring(8..9)}"

@WritingConverter
class PhoneNumberToStringConverter : Converter<PhoneNumber, String> {

    override fun convert(source: PhoneNumber): String {
        return source.toE164Format()
    }

}

@ReadingConverter
class StringToPhoneNumberConverter : Converter<String, PhoneNumber> {

    override fun convert(source: String): PhoneNumber {
        val nationalNumber = source.takeLast(10)
        val countryCode = source.substringBefore(nationalNumber)
        return PhoneNumber(countryCode, nationalNumber)
    }

}
