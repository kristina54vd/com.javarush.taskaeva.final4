# Проект: Сравнение производительности MySQL и Redis + миграция на PostgreSQL

## Описание
Проект демонстрирует:
1. Сравнение скорости выполнения запросов к MySQL и Redis
2. Подготовку миграций для переезда с MySQL на PostgreSQL через Liquibase

## Технологии
- Java 20
- Hibernate 5.6
- MySQL 8.0 (исходная БД)
- PostgreSQL 15 (целевая БД — миграция)
- Redis (Lettuce client)
- Liquibase 4.25 (миграции)
- Maven
- Docker / Docker Compose

## Запуск проекта

### Вариант 1: Работа с MySQL (исходная система)
```bash
# Запуск контейнеров
docker-compose up -d mysql redis

# Применение миграций к MySQL
docker-compose up liquibase-mysql

# Запуск приложения
Запустить класс Main в IntelliJ IDEA
Вариант 2: Миграция на PostgreSQL (новая система)
bash
# Запуск контейнеров с PostgreSQL
docker-compose up -d postgres redis

# Применение миграций к PostgreSQL
docker-compose up liquibase-postgres

# Импорт данных в PostgreSQL (опционально)
docker cp changelog/sql/world.sql postgres:/tmp/
docker exec -it postgres psql -U postgres -d world -f /tmp/world.sql

# Запуск приложения с PostgreSQL (изменить URL в конфиге)
Остановка всех контейнеров
bash
docker-compose down
Структура Liquibase миграций
text
changelog/
├── master.yaml                           # главный файл
├── structural/create-base-tables.yaml   # создание таблиц
├── import/import-raw-data.yaml           # импорт данных
├── convert/convert-data.yaml             # преобразование данных
└── constraints/apply-constraints.yaml    # внешние ключи и индексы
Проблемы, решённые при миграции с MySQL на PostgreSQL
Проблема	Решение
MySQL-specific типы (TINYINT(1), AUTO_INCREMENT)	Использованы универсальные типы Liquibase (INT, BOOLEAN)
Кавычки (`backticks` vs "double quotes")	Liquibase автоматически конвертирует
INSERT IGNORE	Замена на стандартный INSERT или ON CONFLICT
Схема world в аннотациях Hibernate	Убрана схема из @Table для кросс-БД совместимости
Структура проекта
dao/ — Data Access Object (CityDAO, CountryDAO)

domain/ — Entity-классы (City, Country, CountryLanguage)

redis/ — DTO для Redis (CityCountry, Language)

changelog/ — Liquibase миграции (работают на MySQL и PostgreSQL)

Результаты тестирования
Приложение показывает время выполнения запросов к MySQL и Redis

Liquibase миграции успешно применяются к обеим базам данных

Теоретически возможен переезд с MySQL на PostgreSQL без изменения Java-кода
