# QA Automation Portfolio

[![Tests](https://github.com/ch41/qa-automation-portfolio/actions/workflows/tests.yml/badge.svg?branch=master)](https://github.com/ch41/qa-automation-portfolio/actions/workflows/tests.yml)
[![Allure Report](https://img.shields.io/badge/Allure-report-blue)](https://ch41.github.io/qa-automation-portfolio/)

Портфолио-проект по автоматизации тестирования: UI-тесты SauceDemo, API-тесты на JSONPlaceholder и WireMock, мобильный smoke-тест на ApiDemos.

## Стек

| Компонент | Технология |
|---|---|
| Язык | Java 21 |
| Сборка | Gradle 9.6, Kotlin DSL |
| UI-тесты | Selenide 7.19, Selenium 4.51 |
| API-тесты | RestAssured 6.2, JSON Schema Validator |
| API mocking | WireMock 3.13 |
| Mobile | Appium Java Client 11 |
| Test runner | JUnit 5.14, JUnit Pioneer 2.3 (retry) |
| Отчётность | Allure 2.35 (библиотеки), Allure commandline 2.46.1 |
| Проверки | AssertJ 3.27, Hamcrest |
| CI | GitHub Actions |

## Структура проекта

```text
src/test/java/com/qaautomation/portfolio
├── api
│   ├── client          # RestAssured-клиенты
│   ├── data            # API-тестовые данные
│   ├── model           # API-модели
│   ├── spec            # request/response specifications
│   └── tests           # API-тесты
├── config              # загрузка конфигурации
├── core
│   ├── api             # база API-тестов
│   ├── mobile          # драйвер и база мобильных тестов
│   └── web             # настройка Selenide и Allure
├── mobile
│   ├── screens         # мобильные экраны
│   └── tests           # мобильные тесты
├── utils               # TestData, JsonUtils
└── web
    ├── pages           # Page Objects и компоненты
    └── tests           # UI-тесты
```

## Требования

- JDK 21.
- Google Chrome для UI-тестов.
- Mozilla Firefox для cross-browser smoke.
- Gradle Wrapper входит в проект.
- Для мобильных тестов: Appium server, Android emulator/device и установленное приложение.

## Конфигурация

Основные настройки находятся в:

```text
src/test/resources/config/config.properties
```

Локальные переопределения:

```text
src/test/resources/config/local.properties
```

Приоритет значений:

```text
системные свойства → переменные окружения → local.properties → config.properties
```

Примеры:

```text
-Dweb.base.url=https://www.saucedemo.com
-Dweb.browser=chrome
-Dweb.headless=true
-Dweb.timeout=10000
```

В PowerShell аргументы `-D` нужно заключать в кавычки:

```powershell
.\gradlew.bat webTest "-Dweb.headless=true"
```

## Тестовые данные

```text
src/test/resources/testdata
├── users.json       # пользователи SauceDemo
├── products.json    # товары, цены и ожидаемые порядки сортировок
├── checkout.json    # покупатель, оплата, доставка и суммы заказов
└── api.json         # ID, payloads и сообщения ошибок API
```

Доступ к web-данным централизован в `TestData`, к API-данным — в `ApiTestData`. JSON Schema лежат в `src/test/resources/schemas`.

## Параллельность и retry

Настройки JUnit Platform:

```text
src/test/resources/junit-platform.properties
```

По умолчанию включены параллельные классы и методы, `fixed.parallelism=2`. Selenide держит отдельный WebDriver в каждом потоке.

Переопределение из командной строки:

```powershell
.\gradlew.bat webTest "-Djunit.jupiter.execution.parallel.config.fixed.parallelism=4"
```

Retry точечный: аннотация `@RetryOnFailure` даёт 2 попытки для известных нестабильных UI-тестов. Глобального retry нет, чтобы не скрывать реальные дефекты.

## Запуск тестов

```powershell
# Все API и web-тесты, mobile исключён
.\gradlew.bat test

# Только API-тесты
.\gradlew.bat apiTest

# Только web-тесты
.\gradlew.bat webTest "-Dweb.headless=true"

# Smoke для cross-browser matrix
.\gradlew.bat webSmokeTest "-Dweb.browser=chrome" "-Dweb.headless=true"
.\gradlew.bat webSmokeTest "-Dweb.browser=firefox" "-Dweb.headless=true"

# Полный web-набор в другом браузере
.\gradlew.bat webTest "-Dweb.browser=edge" "-Dweb.headless=true"

# Только mobile-тесты
.\gradlew.bat mobileTest

# API + web + mobile
.\gradlew.bat allTests
```

Для Linux/macOS:

```bash
./gradlew webTest -Dweb.browser=chrome -Dweb.headless=true
```

Локально на Windows доступны Chrome, Edge и Firefox (если установлен). Safari требует macOS или облачную ферму.

## Теги

| Тег | Назначение |
|---|---|
| `web` | UI-тесты, наследуется от `BaseWebTest` |
| `api` | API-тесты |
| `mobile` | Мобильные тесты |
| `smoke` | Критичные сценарии |

`webTest` запускает тесты с тегом `web`, `apiTest` — с тегом `api`, `mobileTest` — с тегом `mobile`, `webSmokeTest` — с тегом `smoke` без API и mobile.

Smoke-набор для матрицы: login, каталог, корзина, checkout и logout — 5 тестов.

## Allure

Результаты тестов записываются в:

```text
build/allure-results
```

Сборка HTML-отчёта:

```powershell
.\gradlew.bat allureReport --clean
```

Отчёт создаётся в:

```text
build/reports/allure-report/allureReport
```

Публичная версия: [Allure Report](https://ch41.github.io/qa-automation-portfolio/).

Отчёт публикуется автоматически из `master` только после успешных job `api-and-web` и `web-smoke`.

`--clean` очищает только HTML-отчёт, но не результаты прошлых прогонов. Для чистого локального отчёта запускайте тесты после `clean`:

```powershell
.\gradlew.bat clean apiTest webTest
.\gradlew.bat allureReport --clean
```

Локальный просмотр:

```powershell
.\gradlew.bat allureServe
```

Что попадает в отчёт:

- шаги Selenide;
- скриншоты и page source при падении;
- API-запросы и ответы;
- иерархия `Epic → Feature → Story`;
- окружение: браузер, размер окна, headless, base URL.

`AllureSelenide` подключается в `BaseWebTest`.

## CI

Workflow:

```text
.github/workflows/tests.yml
```

Запускается на:

- push в `main` или `master`;
- pull request;
- ручной запуск.

Pipeline:

1. Checkout.
2. JDK 21.
3. Кэш Gradle.
4. `apiTest` и полный `webTest` в Chrome headless; JUnit-параллельность — 2 потока.
5. Параллельная матрица `webSmokeTest`: Chrome и Firefox.
6. Генерация Allure-отчёта и загрузка `allure-results`/HTML как artifacts.

`mobileTest` в CI не запускается: нужны Appium и Android-устройство. Для известных флейков используется точечный `@RetryOnFailure`, а не глобальный retry.

## Покрытие

Web: **40 тестов**.

| Модуль | Тестов |
|---|---:|
| Login | 6 |
| Access control | 5 |
| Inventory | 15 |
| Product details | 2 |
| Cart | 4 |
| Checkout happy path | 2 |
| Checkout validation | 4 |
| Checkout cancel | 2 |

API: **12 тестов**.

| Модуль | Тестов |
|---|---:|
| JSONPlaceholder: CRUD, фильтры, комментарии, JSON Schema | 8 |
| WireMock: 401, 404, 500, malformed JSON | 4 |

Mobile: **1 smoke-тест** на ApiDemos.

Подробнее:

- [Тест-план](docs/test-plan.md)
- [Тест-кейсы](docs/test-cases.md)

## Ограничения

- API-тесты не относятся к SauceDemo; JSONPlaceholder не сохраняет изменения, поэтому PUT/DELETE проверяют ответ, а не состояние. Негативные сценарии изолированы в WireMock.
- Mobile-тест проверяет только запуск демо-приложения.
- Cross-browser matrix ограничена smoke-набором в Chrome и Firefox; полный набор идёт в Chrome.
- Нет Safari/WebKit и Edge в CI.
- Параллельность ограничена 2 потоками; при увеличении нужно следить за CPU, памятью и нагрузкой на SauceDemo.
- Retry применяется только к известным нестабильным тестам и не заменяет исправление причин флейков.
- Нет проверок accessibility и performance.
- `Reset App State` в SauceDemo не обновляет кнопку товара до повторного входа в каталог; тест учитывает это поведение.

## Roadmap

- [ ] Cross-browser: Edge и Safari/WebKit.
- [ ] Увеличение parallelism после стабилизации suite.
- [ ] Мониторинг flake rate и удаление временных retry.
- [ ] Checkout с пустой корзиной.
- [ ] Публикация Allure-отчёта.
- [ ] Расширение mobile-покрытия.
