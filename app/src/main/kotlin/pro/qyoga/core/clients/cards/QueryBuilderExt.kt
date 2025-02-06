package pro.qyoga.core.clients.cards

import org.springframework.data.relational.core.query.Criteria
import pro.qyoga.core.clients.cards.model.PhoneNumber
import kotlin.reflect.KProperty1


infix fun KProperty1<*, PhoneNumber?>.isPhoneNumberILikeIfNotNull(value: String?) =
    if (value != null) {
        isPhoneNumberILike(value)
    } else {
        Criteria.empty()
    }

infix fun KProperty1<*, PhoneNumber?>.isPhoneNumberILike(value: String) =
    Criteria.where(this.name).like("%$value%").ignoreCase(true)

