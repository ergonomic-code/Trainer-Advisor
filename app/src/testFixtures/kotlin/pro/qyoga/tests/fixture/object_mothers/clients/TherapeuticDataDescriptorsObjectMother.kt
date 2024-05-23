package pro.qyoga.tests.fixture.object_mothers.clients

import pro.azhidkov.platform.extensible_entity.descriptor.CustomFieldType
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataBlock
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptor
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataField
import pro.qyoga.core.users.therapists.TherapistRef
import pro.qyoga.tests.fixture.data.randomCyrillicWord
import pro.qyoga.tests.fixture.object_mothers.therapists.THE_THERAPIST_REF
import pro.qyoga.tests.fixture.object_mothers.therapists.TherapistsObjectMother
import kotlin.random.Random


object TherapeuticDataDescriptorsObjectMother {

    val defaultTherapeuticDataDescriptor = TherapeuticDataDescriptor(
        TherapistsObjectMother.fakeTherapistRef,
        listOf(
            TherapeuticDataBlock(
                "Жалобы",
                listOf(
                    TherapeuticDataField(
                        "Что заставило Вас обратиться к терапевту? Что вас беспокоит?",
                        CustomFieldType.TEXT,
                        true
                    ),
                    TherapeuticDataField(
                        "Как давно появились жалобы?",
                        CustomFieldType.TEXT,
                        true
                    ),
                    TherapeuticDataField(
                        "Принимаете ли какие-либо лекарства на постоянной основе?",
                        CustomFieldType.TEXT,
                        true
                    )
                )
            ),
            TherapeuticDataBlock(
                "Анамнез",
                listOf(
                    TherapeuticDataField(
                        "Какие ещё заболевания вы переносили ранее?",
                        CustomFieldType.TEXT,
                        true
                    )
                ),
            )
        )
    )

    fun therapeuticDataDescriptor(
        vararg blocks: TherapeuticDataBlock,
        owner: TherapistRef = THE_THERAPIST_REF,
    ) = TherapeuticDataDescriptor(
        owner,
        blocks.toList()
    )

    fun therapeuticDataDescriptor(
        blocksCount: Int = 1,
        fieldsPerBlock: Int = 1,
        owner: TherapistRef = THE_THERAPIST_REF
    ) = TherapeuticDataDescriptor(
        owner,
        (1..blocksCount).map { therapeuticDataBlock(fieldsPerBlock) }
    )

    fun therapeuticDataBlock(fields: Int = 1) = TherapeuticDataBlock(
        randomCyrillicWord(),
        (1..fields).map { therapeuticDataField() }
    )

    fun therapeuticDataBlock(vararg fields: TherapeuticDataField) = TherapeuticDataBlock(
        randomCyrillicWord(),
        fields.toList()
    )

    fun therapeuticDataField() = TherapeuticDataField(
        randomCyrillicWord(),
        CustomFieldType.entries.random(),
        Random.nextBoolean()
    )

}