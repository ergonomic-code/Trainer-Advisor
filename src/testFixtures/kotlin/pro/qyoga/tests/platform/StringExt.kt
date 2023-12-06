package pro.qyoga.tests.platform


fun String.pathToRegex() = this.replace("\\{.+}".toRegex(), ".+")