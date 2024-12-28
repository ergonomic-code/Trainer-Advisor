package pro.azhidkov.platform.spring.sdj

import org.springframework.data.mapping.PersistentEntity
import org.springframework.data.mapping.PersistentProperty


fun <P : PersistentProperty<P>, E> PersistentEntity<E, P>.getPersistentProperties(): List<PersistentProperty<*>> =
    buildList {
        this@getPersistentProperties.doWithProperties { add(it) }
    }
