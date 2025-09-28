package pro.azhidkov.platform.secrets

data class SecretChars(
    private val value: CharArray
) {

    fun show() =
        String(value)

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SecretChars

        return value.contentEquals(other.value)
    }

    override fun hashCode(): Int {
        return value.contentHashCode()
    }

    override fun toString(): String {
        return "<hidden>"
    }

}
