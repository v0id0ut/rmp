# Конев Глеб ИВТ-23-1 РМП

# Tandem (SkillSwap) — Приложение для обмена навыками

Мобильное Android-приложение для поиска партнеров и взаимного обучения (бартера знаниями и умениями).

## Ссылка на макет
* **Figma Design:** [Открыть макет Tandem](https://www.figma.com/design/UQGRSyqf89oQw7iRZubAoB/Untitled?node-id=0-1&t=t8Dw8iuRLzjTN7PF-1)

## Основной функционал
* **Авторизация и профиль:** регистрация, вход и настройка личных компетенций (чему могу научить / чему хочу научиться).
* **Каталог навыков:** просмотр доступных предложений от других пользователей с фильтрацией и поиском.
* **Создание карточек:** публикация собственных запросов на обмен навыками.
* **Мои обмены (Swaps):** управление активными и завершенными заявками на взаимное обучение.
* **Избранное и уведомления:** сохранение интересных анкет и отслеживание статуса откликов.

## Стек технологий и архитектура
* **Язык:** Kotlin
* **Интерфейс:** XML, Material Design Components, `BottomNavigationView`, `RecyclerView`
* **Архитектура:** MVVM (Model-View-ViewModel)
* **Асинхронность и реактивность:** Kotlin Coroutines, `StateFlow`
* **Хранение данных:** `SharedPreferences` (локальное сохранение сессии и пользовательских настроек)
* **Навигация:** Fragment Manager / Navigation Component (10 экранов)

---

## Скриншоты приложения

### 1. Экран входа и регистрации
<img width="1080" height="2400" alt="Screenshot_20261028_164827" src="https://github.com/user-attachments/assets/d3cb09e5-ab51-4f5c-aa7e-0ccedf8edf73" />


### 2. Главная лента предложений
<img width="1080" height="2400" alt="Screenshot_20261028_164945" src="https://github.com/user-attachments/assets/ca3c5506-0199-4fd8-8ecb-13b62472a643" />


### 3. Детальная карточка навыка
<img width="1080" height="2400" alt="Screenshot_20261028_165036" src="https://github.com/user-attachments/assets/30c28fb0-c54b-4255-b355-ed1323e03680" />


### 4. Создание нового предложения
<img width="1080" height="2400" alt="Screenshot_20261028_165057" src="https://github.com/user-attachments/assets/ea627a51-f096-43ab-bc06-c81b690f078e" />


### 5. Мои обмены (Активные и завершенные)
<img width="1080" height="2400" alt="Screenshot_20261028_165115" src="https://github.com/user-attachments/assets/6c2fe2af-6c1a-417f-bf6e-73530b72ccd0" />


### 6. Профиль пользователя и настройки
<img width="1080" height="2400" alt="Screenshot_20261028_165124" src="https://github.com/user-attachments/assets/4b260660-57bf-410b-a658-8af86b549c1d" />


---

## Запуск проекта
1. Склонируйте репозиторий:
   `git clone https://github.com/v0id0ut/rmp.git`
2. Откройте проект в **Android Studio**.
3. Дождитесь завершения синхронизации Gradle и запустите приложение на эмуляторе или физическом устройстве.
