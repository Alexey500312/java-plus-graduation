# Список сервисов core

## user-service

Сервис для работы с пользователями

## event-service

Сервис для работы с событиями.<br>
Сервис взаимодействует с:
1. user-service
2. request-service

## request-service

Сервис для работы с запросами на участие.<br>
Сервис взаимодействует с:
1. user-service
2. event-service

## comment-service

Сервис для работы с комментариями к событиям.<br>
Сервис взаимодействует с:
1. user-service
2. event-service

## stats-server

Сервис статистики.

# Ссылки на спецификацию

1. [Главный сервис](ewm-main-service-spec.json)
2. [Сервис статистики](ewm-stats-service-spec.json)