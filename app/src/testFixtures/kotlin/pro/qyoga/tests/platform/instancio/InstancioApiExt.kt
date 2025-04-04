package pro.qyoga.tests.platform.instancio

import org.instancio.InstancioApi
import org.instancio.Random
import org.instancio.TargetSelector
import org.instancio.generator.AfterGenerate
import org.instancio.generator.Generator
import org.instancio.generator.Hints


fun <T, V> InstancioApi<T>.generateBy(selector: TargetSelector, gen: () -> V): InstancioApi<T> =
    generate(selector, object : Generator<V> {
        override fun generate(random: Random?): V? =
            gen()

        override fun hints(): Hints? {
            return Hints.builder().afterGenerate(AfterGenerate.DO_NOT_MODIFY).build()
        }
    })
