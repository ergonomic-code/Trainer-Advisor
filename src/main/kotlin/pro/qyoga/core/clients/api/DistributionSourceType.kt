package pro.qyoga.core.clients.api

import pro.qyoga.platform.kotlin.LabeledEnum

enum class DistributionSourceType(override val label: String) : LabeledEnum {
    SOCIAL_NETWORKS("Социальные сети"),
    FRIEND_REFERRAL("Совет знакомых"),
    SPECIALIST_REFERRAL("Направление специалиста"),
    GEOSERVICES("2Гис, Яндекс.Карты и т.д."),
    OTHER("Другое")
}