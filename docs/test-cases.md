# Тест-кейсы

## Общие предусловия

- SauceDemo доступен.
- Браузер запускается Selenide.
- Для позитивных сценариев используется `standard_user / secret_sauce`.
- Корзина пуста после логина.
- Товары, цены, покупатель и ожидаемые суммы checkout берутся из `src/test/resources/testdata`.
- Для API используется `https://jsonplaceholder.typicode.com`.
- Для mobile запущены Appium и Android emulator/device.

## Обозначения

| Поле | Значение |
|---|---|
| Статус | `Automated` — покрыт автоматическим тестом |
| Приоритет | `P1` — критичный, `P2` — основной, `P3` — дополнительный |

## 1. Авторизация

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-AUTH-001 | Вход с валидными данными | Открыть сайт → ввести `standard_user / secret_sauce` → Login | Открывается каталог, отображается 6 товаров | P1 | `LoginTest.shouldLoginWithValidCredentials` |
| TC-AUTH-002 | Вход с неверными данными | Ввести `invalid_user / invalid_password` → Login | Остаёмся на странице логина, ошибка `Username and password do not match` | P1 | `LoginTest.shouldShowErrorForInvalidCredentials` |
| TC-AUTH-003 | Вход заблокированного пользователя | Ввести `locked_out_user / secret_sauce` → Login | Ошибка `Sorry, this user has been locked out` | P1 | `LoginTest.shouldRejectLockedUser` |
| TC-AUTH-004 | Пустые логин и пароль | Оставить оба поля пустыми → Login | Ошибка `Username is required` | P1 | `LoginTest.shouldRequireUsernameWhenFieldsAreEmpty` |
| TC-AUTH-005 | Пустой пароль | Ввести логин, пароль пустой → Login | Ошибка `Password is required` | P1 | `LoginTest.shouldRequirePasswordWhenUsernameIsFilled` |
| TC-AUTH-006 | Закрытие ошибки логина | Ввести неверные данные → Login → закрыть ошибку | Сообщение исчезает | P3 | `LoginTest.shouldCloseLoginError` |
| TC-AUTH-007 | Защищённые страницы после logout | Logout → открыть `/inventory.html`, `/cart.html`, `/checkout-step-one.html`, `/checkout-step-two.html`, `/checkout-complete.html` | Каждый URL перенаправляет на страницу логина с пустыми полями | P1 | `AuthGuardTest.shouldRedirectProtectedPageToLoginAfterLogout` |

## 2. Каталог

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-INV-001 | Отображение каталога | Войти стандартным пользователем | Отображается 6 товаров | P1 | `InventoryTest.shouldDisplaySixProductsAfterLogin` |
| TC-INV-002 | Сортировка по имени A–Z | Выбрать `Name (A to Z)` | Товары идут в алфавитном порядке | P2 | `InventoryTest.shouldSortAlphabeticallyASCCorrectly` |
| TC-INV-003 | Сортировка по имени Z–A | Выбрать `Name (Z to A)` | Товары идут в обратном алфавитном порядке | P2 | `InventoryTest.shouldSortAlphabeticallyDESCCorrectly` |
| TC-INV-004 | Сортировка по цене возрастанию | Выбрать `Price (low to high)` | Цены идут от `$7.99` до `$49.99` | P2 | `InventoryTest.shouldSortPriceASCCorrectly` |
| TC-INV-005 | Сортировка по цене убыванию | Выбрать `Price (high to low)` | Цены идут от `$49.99` до `$7.99` | P2 | `InventoryTest.shouldSortPriceDESCCorrectly` |
| TC-INV-006 | Поля карточки товара | Открыть каталог | У каждого товара есть изображение, имя, описание, цена и кнопка | P2 | `InventoryTest.shouldDisplayAllProductFields` |
| TC-INV-007 | Открытие и закрытие меню | Нажать бургер → Close | Меню открывается и закрывается | P2 | `InventoryTest.shouldOpenAndCloseBurgerMenu` |
| TC-INV-008 | Logout | Открыть меню → Logout | Открывается страница логина, поля пустые | P1 | `InventoryTest.shouldLogoutUserFromBurgerMenu` |
| TC-INV-010 | Состав burger menu | Открыть меню | Отображаются All Items, Dynamic Catalog, About, Logout, Reset App State | P2 | `InventoryTest.shouldContainAllBurgerMenuFields` |
| TC-INV-011 | All Items | Открыть карточку товара → меню → All Items | Открывается каталог | P2 | `InventoryTest.shouldReturnToInventoryFromAllItemsMenu` |
| TC-INV-012 | Reset App State | Добавить товар → меню → Reset App State → закрыть меню | Корзина пуста, badge отсутствует; после возврата в каталог кнопка снова `Add to cart` | P2 | `InventoryTest.shouldResetAppStateFromBurgerMenu` |
| TC-INV-013 | Добавление одного товара | Нажать `Add to cart` у Backpack | Кнопка меняется на `Remove`, badge равен `1` | P1 | `InventoryTest.shouldAddProductToCart` |
| TC-INV-014 | Добавление нескольких товаров | Добавить 3 разных товара | У всех товаров `Remove`, badge равен `3` | P1 | `InventoryTest.shouldAddMultipleProductsToCart` |
| TC-INV-015 | Добавление всех товаров | Нажать `Add to cart` у всех 6 товаров | Все кнопки `Remove`, badge равен `6` | P2 | `InventoryTest.shouldAddAllProductsToCart` |
| TC-INV-016 | Открытие карточки товара | Нажать на название Backpack | Открывается страница товара с корректными именем, ценой и описанием; Back возвращает в каталог | P1 | `InventoryTest.shouldOpenInventoryItem` |

## 3. Карточка товара

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-ITEM-001 | Добавление товара со страницы товара | Открыть Backpack → Add to cart | Кнопка `Remove`, badge `1`, товар есть в корзине с ценой `$29.99` и количеством `1` | P1 | `InventoryItemPageTest.shouldAddProductToCartFromDetailsPage` |
| TC-ITEM-002 | Удаление товара со страницы товара | Добавить Backpack → Remove | Кнопка снова `Add to cart`, badge отсутствует, корзина пуста | P1 | `InventoryItemPageTest.shouldRemoveProductFromDetailsPage` |

## 4. Корзина

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-CART-001 | Отображение добавленного товара | Добавить Backpack → открыть корзину | Товар, цена `$29.99`, количество `1`, badge `1` | P1 | `CartTest.shouldDisplayAddedProductInCart` |
| TC-CART-002 | Удаление товара из корзины | Добавить Backpack → корзина → Remove | Корзина пуста, badge отсутствует | P1 | `CartTest.shouldRemoveProductFromCart` |
| TC-CART-003 | Возврат в каталог | Добавить товар → корзина → Continue Shopping | Открывается каталог с 6 товарами | P2 | `CartTest.shouldReturnToInventoryFromCart` |
| TC-CART-004 | Удаление одного товара из нескольких | Добавить Backpack и Bike Light → корзина → Remove у Bike Light | В корзине остаётся Backpack, badge равен `1` | P2 | `CartTest.shouldRemoveOneProductFromMultipleInCart` |

## 5. Checkout

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-CHK-001 | Happy path | Login → add Backpack → cart → checkout → заполнить данные → continue → finish → back home | Подтверждение заказа, суммы `$29.99 / $2.40 / $32.39`, корзина пуста | P1 | `CheckoutTest.shouldCompleteCheckoutWithSingleProduct` |
| TC-CHK-002 | Обязательное имя | Оставить форму пустой → Continue | Ошибка `Error: First Name is required`, остаёмся на шаге checkout | P1 | `CheckoutValidationTest.shouldRequireFirstName` |
| TC-CHK-003 | Обязательная фамилия | Заполнить имя, оставить фамилию пустой → Continue | Ошибка `Error: Last Name is required`, введённое имя сохраняется | P1 | `CheckoutValidationTest.shouldRequireLastName` |
| TC-CHK-004 | Обязательный индекс | Заполнить имя и фамилию, индекс пустой → Continue | Ошибка `Error: Postal Code is required`, введённые данные сохраняются | P1 | `CheckoutValidationTest.shouldRequirePostalCode` |
| TC-CHK-005 | Закрытие ошибки | Пустая форма → Continue → закрыть ошибку | Сообщение исчезает | P3 | `CheckoutValidationTest.shouldCloseValidationError` |
| TC-CHK-006 | Отмена на шаге данных | Открыть checkout → Cancel | Возврат в корзину, товар и badge сохранены | P2 | `CheckoutCancelTest.shouldCancelFromStepOneAndKeepCart` |
| TC-CHK-007 | Отмена на шаге overview | Заполнить данные → Continue → Cancel | Возврат в каталог, товар и badge сохранены | P2 | `CheckoutCancelTest.shouldCancelFromStepTwoAndKeepCart` |
| TC-CHK-008 | Checkout с несколькими товарами | Добавить Backpack и Bike Light → checkout → заполнить данные → finish | Суммы `$39.98 / $3.20 / $43.18`, подтверждение заказа, корзина пуста | P1 | `CheckoutTest.shouldCompleteCheckoutWithMultipleProducts` |

## 6. API

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-API-001 | Получение поста | GET `/posts/1` | Статус `200`, `id = 1`, заголовок не пустой, JSON Schema | P2 | `PostApiTest.shouldReturnPostById` |
| TC-API-002 | Создание поста | POST `/posts` с телом | Статус `201`, создан `id`, заголовок и тело совпадают, JSON Schema | P2 | `PostApiTest.shouldCreatePost` |
| TC-API-003 | Список постов | GET `/posts` | Статус `200`, непустой массив, JSON Schema | P2 | `PostApiTest.shouldReturnPosts` |
| TC-API-004 | Фильтр по пользователю | GET `/posts?userId=1` | Статус `200`, все посты принадлежат `userId = 1` | P2 | `PostApiTest.shouldFilterPostsByUser` |
| TC-API-005 | Ограничение количества | GET `/posts?userId=1&_limit=3` | Статус `200`, ровно 3 поста | P3 | `PostApiTest.shouldLimitPosts` |
| TC-API-006 | Комментарии поста | GET `/posts/5/comments` | Статус `200`, непустой список, все `postId = 5`, JSON Schema | P2 | `PostApiTest.shouldReturnCommentsForPost` |
| TC-API-007 | Обновление поста | PUT `/posts/1` с телом | Статус `200`, `id`, `userId`, заголовок и тело обновлены, JSON Schema | P2 | `PostApiTest.shouldUpdatePost` |
| TC-API-008 | Удаление поста | DELETE `/posts/1` | Статус `200`, тело `{}` | P2 | `PostApiTest.shouldDeletePost` |
| TC-API-009 | Ошибка авторизации | WireMock: GET `/posts/1` → `401` | Статус `401`, заголовок `WWW-Authenticate`, JSON error Schema | P2 | `PostErrorHandlingTest.shouldReturnUnauthorized` |
| TC-API-010 | Ресурс не найден | WireMock: GET `/posts/999999` → `404` | Статус `404`, JSON error Schema | P2 | `PostErrorHandlingTest.shouldReturnNotFound` |
| TC-API-011 | Ошибка сервера | WireMock: GET `/posts/1` → `500` | Статус `500`, JSON error Schema | P2 | `PostErrorHandlingTest.shouldReturnServerError` |
| TC-API-012 | Некорректный JSON | WireMock: GET `/posts/1` → `200` с `not-json` | Тело ответа сохраняется для диагностики | P3 | `PostErrorHandlingTest.shouldPreserveMalformedJson` |

TC-API-009–012 используют WireMock; остальные — JSONPlaceholder.

## 7. Mobile

| ID | Сценарий | Шаги | Ожидаемый результат | Приоритет | Автоматизация |
|---|---|---|---|---|---|
| TC-MOB-001 | Запуск приложения | Запустить настроенное Android-приложение | Открывается ApiDemos, package совпадает с конфигурацией | P3 | `MobileSmokeTest.shouldLaunchConfiguredApp` |

## 8. Не автоматизировано

| ID | Сценарий | Причина |
|---|---|---|
| BL-001 | About и социальные ссылки | Внешние переходы, низкий приоритет |
| BL-002 | Edge и Safari/WebKit в CI | Smoke matrix пока Chrome/Firefox |
| BL-003 | Responsive и mobile web | Не реализовано |
| BL-004 | Accessibility и performance | Не реализовано |
| BL-005 | Полное мобильное покрытие | Только smoke |

## 9. Результат

| Набор | Количество | Статус |
|---|---:|---|
| Web | 40 | Все проходят |
| API | 12 | Все проходят |
| Mobile | 1 | Требуется локальное окружение |

36 документированных web-кейсов; `TC-AUTH-007` параметризован на 5 URL, поэтому запусков 40.

Cross-browser smoke: 5 тестов (`login`, `catalog`, `cart`, `checkout`, `logout`) в Chrome и Firefox.

Web-suite выполняется параллельно в 2 потока. Для известного флейка `InventoryTest.shouldOpenAndCloseBurgerMenu` включены 2 попытки.

Отчёт Allure:

```text
build/allure-results
build/reports/allure-report/allureReport
```
