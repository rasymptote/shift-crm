# Shift Spring Boot CRM Backend

Backend-приложение для управления продавцами и транзакциями с базовой аналитикой.

## Содержание

- [Запуск проекта](#запуск-проекта)
  - [1. Настройка базы данных](#1-настройка-базы-данных)
  - [2. Сборка проекта](#2-сборка-проекта)
  - [3. Запуск приложения](#3-запуск-приложения)
- [Swagger / OpenAPI](#swagger--openapi)
- [REST API](#rest-api)
  - [Sellers](#sellers)
  - [Transactions](#transactions)
  - [Seller Analytics](#seller-analytics)
- [Примеры ответов](#примеры-ответов)
- [Тестирование](#тестирование)

---

## Запуск проекта

### 1. Настройка базы данных

Настройки базы данных находятся в: `src/main/resources/application.yaml`
По умолчанию используется PostgreSQL:

```yaml
url: jdbc:postgresql://localhost:5432/postgres
username: postgres
password: password
```

При необходимости замените значения на свои.

---

### 2. Сборка проекта

macOS/Linux:

```zsh
./gradlew build
```

Windows:

```zsh
gradlew.bat build
```

---

### 3. Запуск приложения

macOS/Linux:

```zsh
./gradlew bootRun
```

Windows:

```zsh
gradlew.bat bootRun
```

После запуска приложение будет доступно по адресу: `http://localhost:8080`

---

## Swagger / OpenAPI

- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

---

## REST API

### Sellers

```
GET    /sellers
GET    /sellers/{id}
POST   /sellers
PUT    /sellers/{id}
DELETE /sellers/{id}
```

Список всех продавцов, информация о конкретном продавце, создание нового продавца, обновление информации о продавце, удаление продавца.

Пример запроса:

```bash
curl -X POST "http://localhost:8080/sellers" \
  -H "Content-Type: application/json" \
  -d '{"name":"Ivan","contactInfo":"+7(999)000-00-00"}'
```

### Transactions

```
GET    /transactions
GET    /transactions/{id}
POST   /transactions
GET    /transactions/seller/{sellerId}
```

Список всех транзакций, информация о конкретной транзакции, создание новой транзакции, список всех транзакций продавца.

Пример запроса:

```bash
curl -X POST "http://localhost:8080/transactions" \
  -H "Content-Type: application/json" \
  -d '{"sellerId":1,"amount":150.00,"paymentType":"CARD"}'
```

### Seller Analytics

```
POST /analytics/sellers/top
POST /analytics/sellers/turnover-less-than
```

Получить самого продуктивного продавца, список продавцов с суммой меньше указанной.

Пример запроса:

```bash
curl -X POST "http://localhost:8080/analytics/sellers/top" \
  -H "Content-Type: application/json" \
  -d '{"period":{"from":"2024-01-01T00:00:00","to":"2024-01-31T23:59:59"}}'
```

---

## Примеры ответов

### 200 OK

```json
{
  "id": 1,
  "name": "Ivan",
  "contactInfo": "+7(999)000-00-00",
  "registrationDate": "2025-10-11T06:20:00"
}
```

### 404 Not Found

```json
{
  "title": "Not Found",
  "status": 404,
  "detail": "Seller with id 1 not found",
  "instance": "/sellers/1"
}
```

### 400 Validation Error

```json
{
  "title": "Bad Request",
  "status": 400,
  "detail": "Validation failed",
  "instance": "/sellers",
  "errors": {
    "name": "must not be blank"
  }
}
```

---

## Тестирование

macOS/Linux:

```zsh
./gradlew test
```

Windows:

```zsh
gradlew.bat test
```

Отчет: `build/reports/tests/test/index.html`
