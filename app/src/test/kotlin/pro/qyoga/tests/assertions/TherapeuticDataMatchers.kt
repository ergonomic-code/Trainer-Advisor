package pro.qyoga.tests.assertions

import io.kotest.matchers.collections.shouldMatchInOrder
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataBlock
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataDescriptor
import pro.qyoga.core.clients.therapeutic_data.descriptors.TherapeuticDataField

infix fun TherapeuticDataDescriptor?.shouldMatch(expected: TherapeuticDataDescriptor) {
    this shouldNotBe null
    this!!.ownerRef.id shouldBe expected.ownerRef.id
    this.blocks shouldMatch expected.blocks
}

infix fun TherapeuticDataBlock.shouldMatch(expected: TherapeuticDataBlock) {
    this.label shouldBe expected.label
    this.fields shouldMatchInOrder expected.fields.map { expectedField -> { actual -> actual shouldMatch expectedField } }
}

infix fun TherapeuticDataField.shouldMatch(expected: TherapeuticDataField) {
    this.label shouldBe expected.label
    this.type shouldBe expected.type
    this.required shouldBe expected.required
}

infix fun Iterable<TherapeuticDataBlock>.shouldMatch(expected: Iterable<TherapeuticDataBlock>) {
    this shouldMatchInOrder expected.map { expectedBlock ->
        { actualBlock -> actualBlock shouldMatch expectedBlock }
    }
}