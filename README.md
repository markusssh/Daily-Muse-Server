# Daily Muse - Облачный личный дневник

Учебный проект, демонстрирующий реализацию микросервисной архитектуры на Spring Boot.

## Архитектура системы

- **Gateway Service** - API шлюз для маршрутизации запросов
- **Authentication Service** - аутентификация и управление пользователями  
- **User Service** - управление пользовательскими данными и настройками
- **Note Service** - управление заметками пользователей
- **Review Service** - управление рецензиями на книги и фильмы

---

## Gateway Service

Микросервис-шлюз для централизованной обработки всех входящих запросов с маршрутизацией к соответствующим сервисам.

### Технологический стек
- Java 17, Spring Boot, Spring Cloud Gateway
- WebFlux (реактивное программирование)
- Docker

### Функциональность
- Маршрутизация запросов к микросервисам
- Централизованная аутентификация через AuthFilter
- CORS настройки для фронтенда
- Load balancing для микросервисов

### Маршруты

| Путь | Сервис | Порт | Фильтр |
|------|--------|------|--------|
| `/api/auth/**` | Authentication Service | 8090 | - |
| `/api/user/**` | User Service | 8081 | AuthFilter |
| `/api/note/**` | Note Service | 8082 | AuthFilter |
| `/api/review/**` | Review Service | 8083 | AuthFilter |

### Особенности реализации

- Реактивная архитектура на WebFlux
- AuthFilter проверяет JWT токены через Authentication Service
- Автоматическое добавление заголовка `x-user-id` в защищенные запросы
- Централизованная CORS конфигурация
- Управление через Actuator endpoints

### Конфигурация
- Порт: 8080
- CORS: http://localhost:3000
- Actuator endpoints: /actuator/gateway

---

## Authentication Service

Микросервис для аутентификации пользователей с использованием JWT-токенов.

### Технологический стек
- Java 17, Spring Boot (Web, Security, Data JPA)
- PostgreSQL, JWT, BCrypt
- Docker

### Функциональность
- Регистрация и аутентификация пользователей
- Генерация и валидация JWT-токенов
- Управление пользовательскими настройками
- Logout с инвалидацией токенов

### REST API

**POST /api/auth/register** - Регистрация пользователя
```
Request: { "email": "user@example.com", "password": "password123" }
Response: { "token": "jwt_token" }
```

**POST /api/auth/authenticate** - Вход пользователя
```
Request: { "email": "user@example.com", "password": "password123" }
Response: { "token": "jwt_token" }
```

**POST /api/auth/validate?token={jwt}** - Валидация токена
```
Response: userId (Long)
```

### Модель данных

**User** - пользователь системы
- id, email, password (BCrypt), role

**Settings** - настройки пользователя
- name, color, fontGui, fontNotes

**Token** - JWT токены
- token, tokenType, expired, revoked

### Особенности реализации

- Stateless архитектура с JWT
- Автоматическая инвалидация токенов при logout
- Централизованная обработка исключений
- CORS настройки для фронтенда
- Repository и Service layer паттерны

### Конфигурация
- Порт: 8090
- База данных: PostgreSQL (localhost:5432/daily_muse_user)
- JWT время жизни: 24 часа

---

## User Service

Микросервис для управления пользовательскими данными и персональными настройками.

### Технологический стек
- Java 17, Spring Boot (Web, Security, Data JPA)
- PostgreSQL, Lombok
- Docker

### Функциональность
- Получение пользовательской информации
- Управление персональными настройками
- Изменение темы оформления и шрифтов
- Кастомизация пользовательского интерфейса

### REST API

**GET /api/user/email** - Получение email пользователя
```
Headers: x-user-id: {userId}
Response: "user@example.com"
```

**GET /api/user/settings** - Получение настроек пользователя
```
Headers: x-user-id: {userId}
Response: { "name": "John", "color": 1, "fontGui": 2, "fontNotes": 1 }
```

**PUT /api/user/settings/name?value={name}** - Изменение имени
```
Headers: x-user-id: {userId}
Response: "Name is changed"
```

**PUT /api/user/settings/color?value={color}** - Изменение цветовой темы
```
Headers: x-user-id: {userId}
Response: "Color is changed"
```

**PUT /api/user/settings/font_gui?value={font}** - Изменение шрифта интерфейса
```
Headers: x-user-id: {userId}
Response: "Font for GUI is changed"
```

**PUT /api/user/settings/font_notes?value={font}** - Изменение шрифта заметок
```
Headers: x-user-id: {userId}
Response: "Font for notes is changed"
```

### Модель данных

**User** - пользователь системы
- id, email, password, role

**Settings** - персональные настройки
- id, userId, name, color, fontGui, fontNotes

### Особенности реализации

- Интеграция с Spring Security для UserDetails
- OneToOne связь между User и Settings
- Stateless конфигурация безопасности
- JsonIgnore для предотвращения циклических ссылок
- Валидация через заголовок x-user-id из Gateway

### Конфигурация
- Порт: 8081
- База данных: PostgreSQL (localhost:5432/daily_muse_user)
- Shared database с Authentication Service

---

## Note Service

Микросервис для управления заметками пользователей с поддержкой настроения и календарной организации.

### Технологический стек
- Java 17, Spring Boot (Web, Data JPA)
- PostgreSQL, Lombok
- Docker

### Функциональность
- Создание и редактирование заметок
- Привязка заметок к конкретным датам
- Оценка настроения (1-10)
- Получение заметок за месяц
- Ограничение: одна заметка на день

### REST API

**GET /api/note/month?current_date={date}** - Получение заметок за месяц
```
Headers: x-user-id: {userId}
Response: { "notes": [Note] } | 204 No Content
```

**POST /api/note** - Создание заметки
```
Headers: x-user-id: {userId}
Request: { "content": "text", "mood": 8, "date": "2024-01-15" }
Response: "Note has been created" | 400 "Note already exists"
```

**PUT /api/note** - Обновление заметки
```
Headers: x-user-id: {userId}
Request: { "content": "updated text", "mood": 7, "date": "2024-01-15" }
Response: "Note has been updated" | 400 "Note does not exist"
```

### Модель данных

**Note** - заметка пользователя
- id, userId, content (до 30000 символов), mood (1-10), date

### Особенности реализации

- Кастомные исключения для бизнес-логики
- Проверка существования заметки по дате
- Оптимизированные SQL запросы с JPQL
- Централизованная обработка ошибок
- Валидация через заголовок x-user-id из Gateway

### Конфигурация
- Порт: 8082
- База данных: PostgreSQL (localhost:5433/daily_muse_note)
- Максимальная длина заметки: 30000 символов

---

## Review Service

Микросервис для управления пользовательскими рецензиями на книги и фильмы.

### Технологический стек
- Java 17, Spring Boot (Web, Data JPA)
- PostgreSQL, Lombok
- Docker

### Функциональность
- Создание рецензий на книги и фильмы
- Система оценок (1-10)
- Получение всех рецензий пользователя
- Развернутые текстовые отзывы

### REST API

**GET /api/review/book** - Получение рецензий на книги
```
Headers: x-user-id: {userId}
Response: { "books": [Book] } | 204 No Content
```

**POST /api/review/book** - Создание рецензии на книгу
```
Headers: x-user-id: {userId}
Request: { "title": "Book Title", "author": "Author Name", "rating": 8, "review": "Great book!" }
Response: "Review has been created"
```

**GET /api/review/movie** - Получение рецензий на фильмы
```
Headers: x-user-id: {userId}
Response: { "movies": [Movie] } | 204 No Content
```

**POST /api/review/movie** - Создание рецензии на фильм
```
Headers: x-user-id: {userId}
Request: { "title": "Movie Title", "year": 2024, "rating": 7, "review": "Interesting plot!" }
Response: "Review has been created"
```

### Модель данных

**Book** - рецензия на книгу
- id, userId, title, author, rating (Byte), review (до 30000 символов)

**Movie** - рецензия на фильм  
- id, userId, title, year, rating (Byte), review (до 30000 символов)

### Особенности реализации

- Раздельные контроллеры для книг и фильмов
- Builder pattern для создания объектов
- Sequence generators для автоинкремента ID
- Возврат 204 No Content для пустых коллекций
- Валидация через заголовок x-user-id из Gateway

### Конфигурация
- Порт: 8083 (по умолчанию Spring Boot)
- База данных: PostgreSQL (localhost:5434/daily_muse_review)
- Максимальная длина рецензии: 30000 символов
