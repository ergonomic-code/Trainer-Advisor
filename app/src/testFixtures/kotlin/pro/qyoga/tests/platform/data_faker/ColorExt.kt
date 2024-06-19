package pro.qyoga.tests.platform.data_faker

import net.datafaker.providers.base.Color

fun Color.randomAwtColor(): java.awt.Color =
    java.awt.Color.decode(this.hex())