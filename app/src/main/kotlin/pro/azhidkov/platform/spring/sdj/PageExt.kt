package pro.azhidkov.platform.spring.sdj

import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest

fun <T, R> Page<T>.mapContent(f: (MutableList<T>) -> List<R>): Page<R> {
    return PageImpl(f(this.content), PageRequest.of(this.number, this.size), this.totalElements)
}
