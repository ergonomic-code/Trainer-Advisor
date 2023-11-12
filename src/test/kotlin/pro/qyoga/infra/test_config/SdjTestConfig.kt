package pro.qyoga.infra.test_config

import pro.qyoga.infra.db.SdjConfig
import pro.qyoga.infra.db.testDataSource


object SdjTestConfig {

    val sdjConfig by lazy { SdjConfig({ }, testDataSource) }

}