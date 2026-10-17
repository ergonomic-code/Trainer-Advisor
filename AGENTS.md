# Правила работы с репозиторием

## ErgocodeAi

Этот репозиторий использует ErgocodeAi Agent Framework.
Framework checkout root: `.agents/ergo`.
Framework config path: `.agents/ergo-config.yaml`.
Сохраняй включённым Codex `SessionStart` hook фреймворка.
Hook загружает `.agents/ergo/src/project-baseline.md`, затем общий протокол `.agents/ergo/src/context/index.md` с
отфильтрованными маршрутами и обязательную конфигурацию перед локальными инструкциями этого файла.

## Структура проекта и модули

Trainer Advisor (QYoga) — приложение для йогатерапевтов на Kotlin/Spring Boot.
В `app/src/main/kotlin/pro/qyoga/` разделены точки входа HTTP/UI (`app`), бизнес-логика (`core`), интеграции (`i9ns`) и
инфраструктура (`infra`, `tech`).
Размещай изменения в пакете соответствующей функциональности.
Шаблоны Thymeleaf, статические ресурсы, конфигурация и миграции Flyway находятся в `app/src/main/resources/`.

Тесты приложения размещай в `app/src/test/`, общие фикстуры и инфраструктуру интеграционных тестов — в
`app/src/testFixtures/`.
Браузерные сценарии находятся в `e2e-tests/src/test/`, манифесты развёртывания — в `deploy/`, документация — в `docs/`.

## Команды сборки, тестирования и разработки

Используй Temurin JDK 25, Docker и Docker Compose.
Установи и экспортируй `JAVA_HOME`, указав путь к JDK 25.
Перед каждым вызовом Gradle задавай `GRADLE_USER_HOME=./.gradle-user-home`.

- `GRADLE_USER_HOME=./.gradle-user-home ./gradlew build` — компиляция, тесты и проверки сборки.
- `GRADLE_USER_HOME=./.gradle-user-home ./gradlew :app:test --tests '*PeriodFormatterTest'` — запуск одного тестового
  класса приложения.
- `GRADLE_USER_HOME=./.gradle-user-home ./gradlew :e2e-tests:test` — запуск браузерных сценариев с Chrome/Selenium.
- `GRADLE_USER_HOME=./.gradle-user-home ./gradlew check` — проверки перед отправкой изменений.
- `GRADLE_USER_HOME=./.gradle-user-home ./gradlew :app:bootRun --args='--spring.profiles.active=local-dev,demo'` —
  локальный запуск с инфраструктурой и конфигурацией для разработки.

## Стиль кода и именование

Следуй стандартным [соглашениям JetBrains по стилю Kotlin](https://kotlinlang.org/docs/coding-conventions.html).
Соблюдай существующие роли `*Controller`, `*Op` и `*Repo`.
Компилятор Kotlin считает предупреждения ошибками; конфигурация Detekt находится в `config/detekt/detekt.yml`.

## Правила тестирования

Следуй принятому в соответствующих тестах стилю JUnit Jupiter или Kotest.
Используй имена, описывающие поведение, и структуру Given/When/Then.
Переиспользуй общие фикстуры.
Интеграционные тесты используют Testcontainers, браузерные — Selenide.
Сохраняй настроенные пороги покрытия Kover, включая покрытие контроллеров и операций; точные значения смотри в
`app/build.gradle.kts`.

## Коммиты и пул-реквесты

Следуй формату из истории: `<type>/<task-id>: <description>`, например `build/qg-xxx: обновлены зависимости проекта`.
Ограничивай коммит одной областью изменений.
В пул-реквесте опиши изменения поведения, укажи ссылку на задачу при её наличии и результаты проверок; для изменений
интерфейса приложи скриншоты.
CI собирает пул-реквесты в `master`.

## Безопасность и конфигурация

Не добавляй учётные данные в Git.
Локальные секреты храни в игнорируемом файле `app/src/main/resources/application-local-dev-secrets.yaml`, секреты
развёртывания — в игнорируемом `deploy/host/secrets.sh`.
