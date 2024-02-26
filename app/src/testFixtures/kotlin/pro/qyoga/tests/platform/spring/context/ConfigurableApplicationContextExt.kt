package pro.qyoga.tests.platform.spring.context

import org.springframework.beans.factory.BeanFactory


inline fun <reified T> BeanFactory.getBean(): T =
    this.getBean(T::class.java)
