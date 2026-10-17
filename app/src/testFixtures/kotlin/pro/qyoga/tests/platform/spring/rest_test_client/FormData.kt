package pro.qyoga.tests.platform.spring.rest_test_client

import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap


fun formData(vararg fields: Pair<String, Any?>): MultiValueMap<String, String> {
    return LinkedMultiValueMap<String, String>().apply {
        fields.forEach { (name, value) -> add(name, value?.toString() ?: "") }
    }
}
