package pro.azhidkov.platform.uuid

import java.security.SecureRandom
import java.util.*


/**
 * Источник: https://github.com/0xShamil/uuidv7-kotlin/tree/main
 */
object UUIDv7 {

    private val numberGenerator = SecureRandom()

    /**
     * @return A UUID object representing a UUIDv7 value.
     */
    fun randomUUID(): UUID {
        val value = randomBytes()
        val high = value.toLong(0)
        val low = value.toLong(8)
        return UUID(high, low)
    }

    /**
     * Generates a 16-byte array.
     * The first 6 bytes contain the current timestamp in milliseconds.
     * The next bytes are random, with specific bits set for version and variant.
     *
     * @return A ByteArray of 16 bytes representing the UUIDv7.
     */
    private fun randomBytes(): ByteArray {
        val value = ByteArray(16).also { numberGenerator.nextBytes(it) }

        val timestamp = System.currentTimeMillis()
        value[0] = ((timestamp shr 40) and 0xFF).toByte()
        value[1] = ((timestamp shr 32) and 0xFF).toByte()
        value[2] = ((timestamp shr 24) and 0xFF).toByte()
        value[3] = ((timestamp shr 16) and 0xFF).toByte()
        value[4] = ((timestamp shr 8) and 0xFF).toByte()
        value[5] = (timestamp and 0xFF).toByte()

        // Set the version to 7
        value[6] = ((value[6].toInt() and 0x0F) or 0x70).toByte()

        // Set the variant to IETF variant
        value[8] = ((value[8].toInt() and 0x3F) or 0x80).toByte()

        return value
    }

    /**
     * @param offset The starting index in the ByteArray.
     * @return A Long value constructed from 8 bytes starting at the given offset.
     */
    private fun ByteArray.toLong(offset: Int = 0): Long {
        return ((this[offset].toLong() and 0xFF) shl 56) or
                ((this[offset + 1].toLong() and 0xFF) shl 48) or
                ((this[offset + 2].toLong() and 0xFF) shl 40) or
                ((this[offset + 3].toLong() and 0xFF) shl 32) or
                ((this[offset + 4].toLong() and 0xFF) shl 24) or
                ((this[offset + 5].toLong() and 0xFF) shl 16) or
                ((this[offset + 6].toLong() and 0xFF) shl 8) or
                (this[offset + 7].toLong() and 0xFF)
    }

}