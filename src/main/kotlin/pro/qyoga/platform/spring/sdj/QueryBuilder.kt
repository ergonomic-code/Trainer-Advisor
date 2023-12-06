package pro.qyoga.platform.spring.sdj

import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.CriteriaDefinition
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual
import java.time.LocalDate
import kotlin.reflect.KProperty1

class QueryBuilder {

    private val criteria: List<Criteria> = arrayListOf()

    infix fun <T : Any> KProperty1<*, T>.isEqual(value: T) {
        criteria.addLast(Criteria.where(this.name).isEqual(value))
    }

    infix fun <T> KProperty1<*, T>.isEqualIfNotNull(value: T?) {
        if (value != null) {
            criteria.addLast(Criteria.where(this.name).isEqual(value))
        }
    }

    infix fun KProperty1<*, String?>.isILikeIfNotNull(value: String?) {
        if (value != null) {
            criteria.addLast(Criteria.where(this.name).like("%$value%").ignoreCase(true))
        }
    }

    infix fun KProperty1<*, LocalDate>.isLessThanIfNotNull(value: LocalDate?) {
        if (value != null) {
            criteria.addLast(Criteria.where(this.name).lessThan(value))
        }
    }

    fun build() = Query.query(CriteriaDefinition.from(criteria))

}

fun query(body: QueryBuilder.() -> Unit): Query {
    val builder = QueryBuilder()
    builder.body()
    return builder.build()
}