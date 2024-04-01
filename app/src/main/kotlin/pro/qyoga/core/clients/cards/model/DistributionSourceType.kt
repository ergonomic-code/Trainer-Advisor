package pro.qyoga.core.clients.cards.model

import pro.azhidkov.platform.kotlin.LabeledEnum

enum class DistributionSourceType(override val label: String) : LabeledEnum {
    SOCIAL_NETWORKS("Социальные сети"),
    FRIEND_REFERRAL("Совет знакомых"),
    SPECIALIST_REFERRAL("Направление специалиста"),
    GEOSERVICES("2Гис, Яндекс.Карты и т.д."),
    OTHER("Другое")
}