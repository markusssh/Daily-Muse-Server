# Daily Muse - Микросервисная архитектура

Учебный проект, демонстрирующий реализацию микросервисной архитектуры на Spring Boot.

## Архитектура системы

- **Authentication Service** - аутентификация и управление пользователями

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
```json
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
