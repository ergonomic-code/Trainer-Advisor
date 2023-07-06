package nsu.fit.platform.lang

fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }