package com.example.skillswap

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.json.JSONArray
import org.json.JSONObject

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences("skillswap_prefs", Context.MODE_PRIVATE)

    // Состояния (StateFlow) по требованию архитектуры MVVM
    private val _skills = MutableStateFlow<List<SkillItem>>(emptyList())
    val skills: StateFlow<List<SkillItem>> = _skills

    private val _swaps = MutableStateFlow<List<SwapRequest>>(emptyList())
    val swaps: StateFlow<List<SwapRequest>> = _swaps

    private val _chats = MutableStateFlow<List<ChatThread>>(emptyList())
    val chats: StateFlow<List<ChatThread>> = _chats

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val _reviews = MutableStateFlow<List<ReviewItem>>(emptyList())
    val reviews: StateFlow<List<ReviewItem>> = _reviews

    // Выбранный навык или чат для перехода на детальный экран
    val selectedSkill = MutableStateFlow<SkillItem?>(null)
    val selectedChatPartner = MutableStateFlow("Мария Новикова")

    init {
        loadAllData()
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean("is_logged_in", false)

    fun setLoggedIn(value: Boolean, email: String = "gleb.konev@email.com") {
        prefs.edit().putBoolean("is_logged_in", value).putString("user_email", email).apply()
    }

    private fun loadAllData() {
        // 1. Загрузка навыков (Mock Data из макета Figma)
        val defaultSkills = mutableListOf(
            SkillItem(
                id = 1,
                title = "Основы игры на гитаре",
                category = "Музыка",
                format = "Онлайн или лично",
                duration = "1 час",
                wantInReturn = "В обмен на разбор портфолио",
                authorName = "Мария Новикова",
                authorInitials = "МН",
                rating = 4.9,
                reviewsCount = 18,
                swapsCount = 24,
                descriptionTitle = "Сыграйте первую песню",
                descriptionText = "Помогу освоить базовые аккорды, почувствовать ритм и сыграть любимую песню. Можно с нуля!",
                wantDetails = "Разбор дизайна за 45 минут. Буду рада и другим творческим обменам.",
                nextMeeting = "Ближайшая встреча: сб, 31 октября • После обеда • Бруклин или онлайн"
            ),
            SkillItem(
                id = 2,
                title = "Разговорный испанский",
                category = "Языки",
                format = "Онлайн",
                duration = "45 минут",
                wantInReturn = "В обмен на советы по фото",
                authorName = "Даниэль Руис",
                authorInitials = "ДР",
                rating = 4.9,
                reviewsCount = 12,
                swapsCount = 15,
                descriptionTitle = "Свободное общение без барьеров",
                descriptionText = "Практика живого разговорного испанского с носителем языка. Разберем сленг и полезные фразы для путешествий.",
                wantDetails = "Хочу научиться основам композиции и мобильной фотографии.",
                nextMeeting = "Ближайшая встреча: вс, 1 ноября • Утром • Онлайн"
            ),
            SkillItem(
                id = 3,
                title = "Фингерстайл с нуля",
                category = "Музыка",
                format = "Лично",
                duration = "1 час",
                wantInReturn = "Нужна помощь с Excel",
                authorName = "Ноа Уильямс",
                authorInitials = "НУ",
                rating = 4.8,
                reviewsCount = 9,
                swapsCount = 11,
                descriptionTitle = "Красивые мелодии на акустике",
                descriptionText = "Покажу базовые приемы фингерстайла, постановку рук и разберем простую аранжировку.",
                wantDetails = "Нужна помощь с таблицами Excel, сводными таблицами и базовыми формулами.",
                nextMeeting = "Ближайшая встреча: пн, 2 ноября • Вечером • Лично"
            )
        )

        // Подгружаем созданные пользователем заявки из памяти телефона
        val customSkillsJson = prefs.getString("custom_skills", "[]") ?: "[]"
        val jsonArray = JSONArray(customSkillsJson)
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            defaultSkills.add(
                0,
                SkillItem(
                    id = 100 + i,
                    title = obj.getString("title"),
                    category = obj.optString("category", "Для вас"),
                    format = obj.getString("format"),
                    duration = "1 час",
                    wantInReturn = "В обмен на: ${obj.getString("want")}",
                    authorName = "Глеб Конев",
                    authorInitials = "ГК",
                    rating = 5.0,
                    reviewsCount = 8,
                    swapsCount = 12,
                    descriptionTitle = "Предложение обмена",
                    descriptionText = obj.getString("details"),
                    wantDetails = obj.getString("want"),
                    nextMeeting = "${obj.getString("when")} • ${obj.getString("place")}"
                )
            )
        }
        _skills.value = defaultSkills
        if (selectedSkill.value == null) selectedSkill.value = defaultSkills.first()

        // 2. Загрузка "Мои обмены"
        val defaultSwaps = mutableListOf(
            SwapRequest(1, "Мария Новикова", "МН", "Основы игры на гитаре", "Ваш разбор портфолио", "Сб, 31 окт., 14:00 • Проспект-парк", "ПОДТВЕРЖДЁН"),
            SwapRequest(2, "Даниэль Руис", "ДР", "Разговорный испанский", "Ваши советы по фото", "Ср, 28 окт. • Онлайн • 45 минут", "ЖДЁМ ОТВЕТА")
        )
        for (i in 0 until jsonArray.length()) {
            val obj = jsonArray.getJSONObject(i)
            defaultSwaps.add(
                0,
                SwapRequest(
                    id = 200 + i,
                    partnerName = "Вы (Глеб Конев)",
                    partnerInitials = "ГК",
                    skillTitle = obj.getString("title"),
                    inReturnTitle = obj.getString("want"),
                    dateInfo = "${obj.getString("when")} • ${obj.getString("place")}",
                    status = "АКТИВНА"
                )
            )
        }
        _swaps.value = defaultSwaps

        // 3. Загрузка списка чатов
        _chats.value = listOf(
            ChatThread(1, "Мария Новикова", "МН", "Гитара с нуля ↔ Разбор портфолио", "Отлично! Возьму вторую гитару 🎸", "9:41", 2),
            ChatThread(2, "Даниэль Обрамов", "ДО", "Испанский ↔ Фотография", "Свободны в воскресенье утром?", "Вчера", 1),
            ChatThread(3, "Елена Белых", "ЕБ", "Керамика ↔ UI/UX-дизайн", "Вы: Эти работы просто чудесные!", "Пт", 0),
            ChatThread(4, "Елизавета Русских", "ЕР", "Фингерстайл ↔ Основы Excel", "Спасибо, что написали, Алекс!", "Чт", 0),
            ChatThread(5, "Никита Антропов", "НА", "Йога ↔ Фотография", "Вы: Спасибо за чудесное занятие.", "22 окт", 0)
        )

        // 4. Загрузка сообщений в чате
        val defaultMessages = mutableListOf(
            ChatMessage("Привет, Глеб! Предлагаю урок гитары в обмен на разбор портфолио.", "9:30", false),
            ChatMessage("Отлично! В субботу в 14:00 в Проспект-парке? Я совсем новичок.", "9:32", true),
            ChatMessage("Конечно! Начнём с простых аккордов — опыт не нужен.", "9:34", false),
            ChatMessage("Договорились! Возьму ноутбук: после урока разберём твоё портфолио.", "9:36", true),
            ChatMessage("Отлично! Возьму вторую гитару 🎸", "9:38", false)
        )
        val savedMsgsJson = prefs.getString("saved_messages", "[]") ?: "[]"
        val msgArray = JSONArray(savedMsgsJson)
        for (i in 0 until msgArray.length()) {
            val obj = msgArray.getJSONObject(i)
            defaultMessages.add(ChatMessage(obj.getString("text"), obj.getString("time"), true))
        }
        _messages.value = defaultMessages

        // 5. Загрузка отзывов
        val defaultReviews = mutableListOf(
            ReviewItem("Елена Гаск", "«Глеб объясняет дизайн так понятно! После встречи моё портфолио стало намного лучше».", "12 октября", 5)
        )
        val savedRevJson = prefs.getString("saved_reviews", "[]") ?: "[]"
        val revArray = JSONArray(savedRevJson)
        for (i in 0 until revArray.length()) {
            val obj = revArray.getJSONObject(i)
            defaultReviews.add(0, ReviewItem(obj.getString("author"), obj.getString("text"), obj.getString("date"), obj.getInt("rating")))
        }
        _reviews.value = defaultReviews
    }

    // Сохранение новой заявки в SharedPreferences
    fun createNewRequest(title: String, want: String, details: String, format: String, whenTime: String, place: String) {
        val currentJson = prefs.getString("custom_skills", "[]") ?: "[]"
        val array = JSONArray(currentJson)
        val obj = JSONObject().apply {
            put("title", title)
            put("want", want)
            put("details", details)
            put("format", format)
            put("when", whenTime)
            put("place", place)
            put("category", "Творчество")
        }
        array.put(obj)
        prefs.edit().putString("custom_skills", array.toString()).apply()
        loadAllData()
    }

    // Сохранение нового сообщения в чате
    fun sendMessage(text: String) {
        val currentJson = prefs.getString("saved_messages", "[]") ?: "[]"
        val array = JSONArray(currentJson)
        val obj = JSONObject().apply {
            put("text", text)
            put("time", "Сейчас")
        }
        array.put(obj)
        prefs.edit().putString("saved_messages", array.toString()).apply()
        loadAllData()
    }

    // Сохранение нового отзыва
    fun addReview(partner: String, text: String, rating: Int) {
        val currentJson = prefs.getString("saved_reviews", "[]") ?: "[]"
        val array = JSONArray(currentJson)
        val obj = JSONObject().apply {
            put("author", "Отзыв для: $partner")
            put("text", "«$text»")
            put("date", "Сегодня")
            put("rating", rating)
        }
        array.put(obj)
        prefs.edit().putString("saved_reviews", array.toString()).apply()
        loadAllData()
    }
}