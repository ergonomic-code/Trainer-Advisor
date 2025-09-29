package pro.qyoga.tests.assertions

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.maps.shouldContainKey
import org.springframework.util.MultiValueMap


fun <K, V> MultiValueMap<K, V>.shouldContainValue(key: K, value: V) {
    this shouldContainKey key
    this[key]!! shouldContain value
}