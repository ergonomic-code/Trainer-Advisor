package pro.azhidkov.platform.spring.sdj.query

import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.CriteriaDefinition
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual
import java.time.LocalDate
import kotlin.reflect.KProperty1

enum class BuildMode {
    AND,
    OR
}

class QueryBuilder {

    private val criteria: MutableList<CriteriaDefinition> = arrayListOf()

    var mode = BuildMode.AND

    infix fun <T> KProperty1<*, T>.isEqualIfNotNull(value: T?) {
        if (value != null) {
            criteria.addLast(Criteria.where(this.name).isEqual(value))
        }
    }

    infix fun <T : Any> KProperty1<*, T?>.isEqual(value: T) {
        criteria.addLast(Criteria.where(this.name).isEqual(value))
    }

    infix fun KProperty1<*, String?>.isILikeIfNotNull(value: String?) {
        if (value != null) {
            isILike(value)
        }
    }

    infix fun KProperty1<*, String?>.isILike(value: String) {
        criteria.addLast(Criteria.where(this.name).like("%$value%").ignoreCase(true))
    }

    infix fun KProperty1<*, String?>.containsIfNotNull(value: String?) {
        if (value != null) {
            isILike(value)
        }
    }

    infix fun KProperty1<*, LocalDate>.isLessThanIfNotNull(value: LocalDate?) {
        if (value != null) {
            criteria.addLast(Criteria.where(this.name).lessThan(value))
        }
    }

    fun build() = Query.query(toCriteriaDefinition())

    private fun toCriteriaDefinition() =
        if (mode == BuildMode.AND) {
            CriteriaDefinition.from(criteria)
        } else {
            criteria.fold(Criteria.empty()) { acc, c -> acc.or(c) }
        }

    fun and(subExpression: QueryBuilder.() -> Unit) {
        val subQueryBuilder = QueryBuilder()
        subQueryBuilder.subExpression()
        criteria.addLast(subQueryBuilder.toCriteriaDefinition())
    }

}

fun query(body: QueryBuilder.() -> Unit): Query {
    val builder = QueryBuilder()
    builder.body()
    return builder.build()
}