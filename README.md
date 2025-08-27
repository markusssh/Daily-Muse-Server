# Daily Muse - Облачный личный дневник

Учебный проект, демонстрирующий реализацию микросервисной архитектуры на Spring Boot.

## Архитектура системы

- **Gateway Service** - API шлюз для маршрутизации запросов
- **Authentication Service** - аутентификация и управление пользователями
- **Note Service** - управление заметками пользователей

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
