package pro.qyoga.core.calendar.ical.persistance

import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.qyoga.core.calendar.ical.model.ICalCalendar
import pro.qyoga.core.users.therapists.TherapistRef


private val log = LoggerFactory.getLogger(ICalCalendarsDao::class.java)

@Repository
class ICalCalendarsDao(
    jdbcAggregateTemplate: JdbcAggregateTemplate,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext
) : ErgoRepository<ICalCalendar, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    ICalCalendar::class,
    jdbcConverter,
    relationalMappingContext
)

fun ICalCalendarsDao.findAllByOwner(ownerRef: TherapistRef): List<ICalCalendar> =
    findSlice(PageRequest.ofSize(100)) {
        ICalCalendar::ownerRef isEqual ownerRef
    }
        .also { if (it.hasNext()) log.warn("Therapist $ownerRef has more that 100 calendars") }
        .content