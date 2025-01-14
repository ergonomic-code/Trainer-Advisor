package pro.azhidkov.platform.kotlin


fun <K, V> mapOfNotNull(vararg pairs: Pair<K, V>?): Map<K, V> = mapOf(*pairs.filterNotNull().toTypedArray())
