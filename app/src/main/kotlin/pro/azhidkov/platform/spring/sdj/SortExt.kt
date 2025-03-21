package pro.azhidkov.platform.spring.sdj

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import kotlin.reflect.KProperty1

fun Pageable.withSortBy(property: KProperty1<*, *>) = PageRequest.of(this.pageNumber, this.pageSize, sortBy(property))

fun Pageable.withSortBy(sort: Sort) = PageRequest.of(this.pageNumber, this.pageSize, sort)

fun sortBy(property: KProperty1<*, *>) = Sort.by(property.name)
