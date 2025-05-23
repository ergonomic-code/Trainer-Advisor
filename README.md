# Trainer Advisor

Информационная система йогатерапевта.

Данный проект является иллюстрацией кодовой базы, выполненной в соответствии с [Эргономичным подходом](https://ergowiki.azhidkov.pro/).

Посты с описанием проекта можно найти у меня в [блоге](https://azhidkov.pro/tags/trainer-advisor/) и [Telegram-канале](https://t.me/s/ergonomic_code?q=%23trainer_advisor).

## Требования к инфраструктуре

* JDK: 21 (Temurin)
* PostgreSQL 15.2

## Требования к рабочему окружению

* Docker
* Docker Compose

## Локальная разработка

### TDD

Разработку рекомендуется вести преимущественно через TDD - то есть реализация продового кода ведётся в рамках "
озеленения" теста.

#### Запуск инфраструктуры

Опционально можно руками запустить инфраструктуру для тестов c помощью IDEA Run Configuration (далее - ран конфига) "
Infra - Tests - Up":

![img.png](docs/images/infra-tests-up-screen.png)

Это позволит сэкономить полсекунды на запуск тестов, за счёт пропуска инициализации testcontainers.

_Вообще у IDEA есть специализированные ран конфиги для Docker Compose, но у меня идея крэшится при их использовании_

Остановить инфраструктуру можно ран конфигом "Infra - Tests - Down".

#### Запуск одного теста

В случае простых/небольших фич и багфиксов можно ограничиться запуском одного класса или теста через гуттер

![img.png](docs/images/gutter-screen.png)

#### Запуск быстрых тестов

Если изменения затрагивают несколько частей системы, их разработку можно драйвить с помощью ран конфига "Tests - Fast":

![img.png](docs/images/fast-tests-screen.png)

Эта конфигурация запускает все тесты, не отмеченные тегом "slow" и её есть смысл запускать, если в рамках решения
задачи, был затронут большой процент кода проекта.

#### Запуск всех тестов

Если разработка совсем вышла из-под контроля и починка одного теста влечёт падение другого, то лучше перейти на ран
конфиг "Tests - All":

![img.png](docs/images/all-tests-screen.png)

### Ручное тестирование

Отладка и тестирования вёрстки и динамических частей страниц выполняется в ручном режиме, поэтому для этих целей
необходимо запустить проект с помощью ран конфига "QYogaApp":

![img.png](docs/images/qyoga-app-screen.png)

В проекте настроена
интеграция [Spring Boot Docker Compose](https://docs.spring.io/spring-boot/docs/3.1.0/reference/html/features.html#features.docker-compose),
поэтому для запуска проекта достаточно запустить только само приложение в IDEA.

При изменении статических файлов (и, зачастую, кода) для их обновления не обязательно перезапускать приложение и
достаточно только пересобрать проект (Ctrl+F9).

При необходимости, остановить инфраструктуру можно ран конфигом "Infra - Dev - Down":

![img.png](docs/images/infra-dev-up-screen.png)

### Отладка приложения в контейнере

Для отладки работы приложения в докере можно воспользоваться ран конфигом "System - Local - Up":

![img.png](docs/images/system-local-up-screen.png)

Этот конфиг выполняет сборку приложения в jar-файл (из текущего состояния рабочей директории), после чего собирает
докер-контейнер на базе этого файла и затем в терминале запускает композ всей системы.

Композ запускается в интерактивном режиме, поэтому для остановки надо в консоли ввести Ctrl+c.

### Проверка перед пушем

Перед пушем рекомендуется запускать Gradle-таск check.
Этот таск выполняет все тесты и проверяет процент покрытия кода тестами.
Для запуска таска можно воспользоваться ран конфигом "Tests - Check":

![img.png](docs/images/tests-check-screen.png)

## Локальная разработка фронта в контейнере

### Запуск контейнера

Для запуска проекта необходимо в корне проекта (репозитория) выполнить команду:

```shell
docker compose -f deploy/qyoga/docker-compose-infra-base.yml -f deploy/qyoga/docker-compose-front-dev.yml up --build app 
```

При первом запуске будут длительные задержки - это качаяются зависимости.
Повторые запуски будут проходить быстро.

В случае успешного запуска, в логах докера должна появиться строка

```shell
pro.qyoga.app.QYogaAppKt - Started QYogaAppKt in 1.938 seconds (process running for 2.224)
```

После этого приложение будет доступно по URL http://localhost:8080, а все изменения в HTML/CSS/JavaScript в директории
src/main/resources будут видимы сразу после сохранения файла.

### Пересборка контейнера

Для того чтобы обновить бэк в контейнере (напирмер, после переключения ветки), необходимо удалить старый компоуз-проект:

```shell
docker compose -f deploy/qyoga/docker-compose-infra-base.yml -f deploy/qyoga/docker-compose-front-dev.yml down
```

После чего запустить контейнер заново
