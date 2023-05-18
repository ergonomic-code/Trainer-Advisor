package nsu.fit.qyoga.platform


private object FileObject

fun loadResource(name: String): ByteArray {
    return FileObject::class.java.getResourceAsStream(name)?.readAllBytes()
            ?: throw IllegalArgumentException("Resource $name not found")
}