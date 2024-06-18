package pro.qyoga.core.appointments.types

import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.jdbc.core.JdbcAggregateOperations
import org.springframework.data.jdbc.core.convert.JdbcConverter
import org.springframework.data.relational.core.mapping.RelationalMappingContext
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations
import org.springframework.stereotype.Repository
import pro.azhidkov.platform.spring.sdj.ergo.ErgoRepository
import pro.azhidkov.platform.spring.sdj.sortBy
import pro.qyoga.core.appointments.types.model.AppointmentType
import pro.qyoga.core.users.therapists.TherapistRef

@Repository
class AppointmentTypesRepo(
    jdbcAggregateTemplate: JdbcAggregateOperations,
    namedParameterJdbcOperations: NamedParameterJdbcOperations,
    jdbcConverter: JdbcConverter,
    relationalMappingContext: RelationalMappingContext,
) : ErgoRepository<AppointmentType, Long>(
    jdbcAggregateTemplate,
    namedParameterJdbcOperations,
    AppointmentType::class,
    jdbcConverter,
    relationalMappingContext
) {

    object Page {
        val topFiveByName = PageRequest.of(0, 5, sortBy(AppointmentType::name))
    }

}

fun AppointmentTypesRepo.findTherapistAppointmentTypesSliceByName(
    therapistRef: TherapistRef,
    searchKey: String,
    page: Pageable
): Slice<AppointmentType> {
    return findSlice(page) {
        AppointmentType::ownerRef isEqual therapistRef
        AppointmentType::name isILike searchKey
    }
}
