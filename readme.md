# ⚔️ FFA Backend

### Tournament Management Platform API

<p align="center">
  <strong>REST API для организации, управления и проведения киберспортивных турниров.</strong>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white" alt="Java 25"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-4.1-6DB33F?style=for-the-badge&logo=springboot&logoColor=white" alt="Spring Boot 4.1"/>
  <img src="https://img.shields.io/badge/PostgreSQL-16-4169E1?style=for-the-badge&logo=postgresql&logoColor=white" alt="PostgreSQL 16"/>
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Architecture-DDD-8A2BE2?style=flat-square" alt="DDD"/>
  <img src="https://img.shields.io/badge/Security-JWT-black?style=flat-square&logo=jsonwebtokens" alt="JWT"/>
  <img src="https://img.shields.io/badge/Database-Flyway-CC0200?style=flat-square" alt="Flyway"/>
  <img src="https://img.shields.io/badge/Documentation-OpenAPI-85EA2D?style=flat-square&logo=swagger&logoColor=black" alt="OpenAPI"/>
  <img src="https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square" alt="MIT License"/>
  <img src="https://img.shields.io/badge/Status-Alpha-orange?style=flat-square" alt="Alpha"/>
</p>

---

## 📖 О проекте

**FFA Backend** — серверная часть платформы для организации и проведения киберспортивных турниров.

Проект предоставляет REST API для управления игроками, командами, турнирами и их участниками. Также реализованы механизмы аутентификации, авторизации, интеграции со Steam и поддержки пользовательских аккаунтов.

Проект разрабатывается с использованием принципов **Domain-Driven Design (DDD)** и Clean Architecture, с акцентом на разделение ответственности, расширяемость и тестируемость бизнес-логики.

> 🚧 **Текущая версия: `0.0.1-alpha`**
>
> Проект находится в активной разработке. Архитектура и API могут изменяться.

## ✨ Возможности

| Модуль | Возможности |
|---|---|
| 🔐 Identity | Регистрация, авторизация, JWT-токены, управление ролями |
| 👤 Players | Профили игроков, привязка Steam-аккаунтов через OpenID |
| 👥 Teams | Создание команд, управление составом и приглашения |
| 🏆 Tournaments | Создание турниров, управление участниками и этапами |
| 🛟 Support | Отдельные аккаунты поддержки и bootstrap-конфигурация |
| 📧 Email | Подтверждение смены электронной почты |
| 🗄️ Database | Управление схемой БД и версионирование через Flyway |
| 📚 API Docs | Интерактивная документация OpenAPI / Swagger UI |

## 🛠️ Технологический стек

| Категория | Технологии |
|---|---|
| Language | Java 25 |
| Framework | Spring Boot 4.1 |
| Web | Spring Web MVC |
| Persistence | Spring Data JPA, Hibernate |
| Security | Spring Security, JWT (JJWT 0.12) |
| Validation | Spring Validation |
| Email | Spring Mail |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| API Documentation | Springdoc OpenAPI |
| Testing | JUnit 5, Mockito, Spring Boot Test |
| Build Tool | Maven Wrapper |
| Infrastructure | Docker |

## 🏗️ Архитектура

Проект организован по доменным модулям. Каждый модуль отвечает за собственную область бизнес-логики и разделён на четыре архитектурных слоя.

```text
com.ffa.tournaments
│
├── identity/           # Authentication, authorization, accounts
│
├── player/             # Player profiles, Steam integration
│
├── team/               # Teams, membership, invitations
│
├── tournament/         # Tournaments, brackets, matches
│
├── support/            # Support accounts and administration
│
└── shared/             # Shared infrastructure and utilities
```

### Структура доменного модуля

```text
module/
│
├── domain/
│   ├── model/          # Entities and domain models
│   ├── valueobject/    # Value Objects
│   └── ...             # Domain rules and abstractions
│
├── application/
│   ├── service/        # Application services
│   └── ...             # Use cases
│
├── infrastructure/
│   ├── persistence/    # Database adapters
│   └── ...             # External integrations
│
└── presentation/
    ├── controller/     # REST controllers
    └── dto/            # Request / Response DTOs
```

### Архитектурные принципы

- **Domain-Driven Design** — бизнес-логика организована вокруг предметных областей.
- **Separation of Concerns** — каждый слой имеет чётко определённую ответственность.
- **Dependency Inversion** — доменная логика не должна зависеть от деталей инфраструктуры.
- **Modular Architecture** — функциональность разделена на независимые доменные модули.
- **Testability** — бизнес-правила и сценарии использования удобно покрывать тестами.

## 🚀 Быстрый старт

### Требования

Перед запуском убедитесь, что установлены:

- JDK 25 или новее.
- Docker и Docker Compose (или Docker CLI).
- Git.

> Maven отдельно устанавливать не требуется — проект использует Maven Wrapper.

### 1. Клонирование репозитория

```bash
git clone https://github.com/f1nil4v/ffa-backend.git

cd ffa-backend
```

### 2. Запуск PostgreSQL

Поднимите контейнер с PostgreSQL 16:

```bash
docker run \
  --name ffa-postgres \
  -e POSTGRES_DB=ffa_tournaments \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -p 5432:5432 \
  -d postgres:16
```

### 3. Настройка переменных окружения

Приложение использует следующие переменные окружения:

| Переменная | Назначение | Пример |
|---|---|---|
| `DB_USERNAME` | Пользователь PostgreSQL | `postgres` |
| `DB_PASSWORD` | Пароль PostgreSQL | `postgres` |
| `JWT_SECRET` | Секрет подписи JWT | `your-long-random-secret` |
| `MAIL_USERNAME` | SMTP-логин | `you@yandex.ru` |
| `MAIL_PASSWORD` | SMTP-пароль приложения | `app-password` |
| `SUPPORT_BOOTSTRAP_EMAIL` | Email bootstrap-аккаунта поддержки | `support@ffa.local` |
| `SUPPORT_BOOTSTRAP_PASSWORD` | Пароль bootstrap-аккаунта | `change-me` |
| `STEAM_REALM` | Realm для Steam OpenID | `http://localhost:8080` |
| `STEAM_RETURN_URL` | Callback Steam OpenID | `http://localhost:8080/api/player/steam/link/callback` |

Переменные можно задать в конфигурации IDE или экспортировать в терминале.

**Важно:** для production-окружения обязательно используйте надёжные уникальные секреты и не храните реальные пароли в репозитории.

### 4. Сборка и запуск

Linux / macOS:

```bash
./mvnw clean install
```

Windows:

```powershell
.\mvnw.cmd clean install
```

Запуск приложения:

```bash
./mvnw spring-boot:run
```

После успешного запуска API будет доступно по адресу:

```text
http://localhost:8080
```

### 5. Миграции базы данных

При запуске приложения Flyway автоматически применяет SQL-миграции из:

```text
src/main/resources/db/migration/
```

Это позволяет поддерживать структуру базы данных в актуальном состоянии без ручного выполнения SQL-скриптов.

## 📚 API и документация

Для изучения и тестирования API используется Springdoc OpenAPI.

После запуска приложения документация доступна по следующим адресам:

| Ресурс | URL |
|---|---|
| Swagger UI | http://localhost:8080/swagger-ui.html |
| OpenAPI JSON | http://localhost:8080/v3/api-docs |

### Основные API-модули

| Endpoint | Назначение |
|---|---|
| `/api/auth/**` | Регистрация и аутентификация |
| `/api/player/**` | Профили игроков и Steam |
| `/api/team/**` | Управление командами |
| `/api/tournament/**` | Управление турнирами |

Полный список эндпоинтов, форматы запросов и ответов, а также схемы данных доступны в Swagger UI.

## 📁 Структура проекта

```text
ffa-backend/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/ffa/tournaments/
│   │   │       ├── identity/
│   │   │       ├── player/
│   │   │       ├── team/
│   │   │       ├── tournament/
│   │   │       ├── support/
│   │   │       └── shared/
│   │   │
│   │   └── resources/
│   │       ├── db/
│   │       │   └── migration/
│   │       └── application.yaml
│   │
│   └── test/
│       └── java/
│           └── com/ffa/tournaments/
│
├── mvnw
├── mvnw.cmd
├── pom.xml
├── .gitignore
└── README.md
```

## 🧪 Тестирование

Для запуска тестов используется Maven Wrapper:

```bash
./mvnw test
```

Тестирование включает:

- Unit-тесты доменной логики.
- Тестирование application-сервисов.
- Интеграционные тесты контроллеров.
- Тестирование репозиториев.

## 🗺️ Roadmap

Проект находится на ранней стадии разработки. Основные направления дальнейшего развития:

- [ ] Расширение функциональности турнирного модуля.
- [ ] Развитие системы турнирных сеток и матчей.
- [ ] Улучшение управления командами и приглашениями.
- [ ] Расширение интеграции со Steam.
- [ ] Увеличение покрытия автоматизированными тестами.
- [ ] Улучшение API-документации.
- [ ] Подготовка инфраструктуры для production-развёртывания.

*Roadmap отражает направления развития и не является гарантией сроков реализации.*

## 📄 Лицензия

Проект распространяется под лицензией **MIT**.

Подробности доступны в файле [LICENSE](LICENSE).

---

<p align="center">
  <strong>FFA Backend</strong>
  <br/>
  Tournament Management Platform
  <br/><br/>
  <sub>Built with Java & Spring Boot ☕</sub>
</p>
