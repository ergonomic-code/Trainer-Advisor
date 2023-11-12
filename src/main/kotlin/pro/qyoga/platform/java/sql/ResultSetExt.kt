package pro.qyoga.platform.java.sql

import org.postgresql.util.PGInterval
import pro.qyoga.platform.postgresql.toDuration
import java.sql.ResultSet
import java.time.Duration

inline operator fun <reified T> ResultSet.get(colName: String): T = when (T::class) {
    Long::class -> this.getLong(colName)
    Int::class -> this.getInt(colName)
    String::class -> this.getString(colName)
    Duration::class -> (this.getObject(colName) as PGInterval).toDuration()
    List::class -> (this.getArray(colName).array as Array<*>).toList()
    else -> this.getObject(colName)
} as T