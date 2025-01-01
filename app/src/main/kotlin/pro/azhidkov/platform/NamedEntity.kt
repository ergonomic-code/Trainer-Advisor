package pro.azhidkov.platform

import com.fasterxml.jackson.annotation.JsonCreator


interface NamedEntity<T, ID> {

    val id: ID
    val name: String

    companion object {
        @JsonCreator
        @JvmStatic
        operator fun <T, ID> invoke(id: ID, name: String) = object : NamedEntity<T, ID> {
            override val id = id
            override val name = name
        }
    }

}