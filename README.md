# QYoga

Информационная система йогатерапевта

## Требования к инфраструкутре

* JDK: 17 (Adoptium)

## Требования к рабочему окружению

* Docker
* Docker Compose

## Локальная разрботка

### Запуск приложения в IDEA

В проекте настроена
интеграция [Spring Boot Docker Compose](https://docs.spring.io/spring-boot/docs/3.1.0/reference/html/features.html#features.docker-compose),
поэтому для запуска проекта достаточно запустить IDEA Run Configuration "QyogaApp".

### Запуск приложения в докере

Для запуска приложения в докере его необходимо собрать:

```shell
./gradlew clean build
```

После чего запустить скрипт:

```shell
./deploy/qyoga/run-local.sh
```

Композ запускается в интерактивном режиме, поэтому для остановки надо в консоли ввести Ctrl+c.