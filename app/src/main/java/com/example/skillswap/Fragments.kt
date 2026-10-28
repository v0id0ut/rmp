package com.example.skillswap

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class LoginFragment : Fragment(R.layout.fragment_login) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etEmail = view.findViewById<EditText>(R.id.etEmail)
        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val btnAppleLogin = view.findViewById<Button>(R.id.btnAppleLogin)
        val tvJoinNow = view.findViewById<TextView>(R.id.tvJoinNow)

        val doLogin = {
            viewModel.setLoggedIn(true, etEmail.text.toString())
            (activity as? MainActivity)?.setBottomNavVisible(true)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }

        btnLogin.setOnClickListener { doLogin() }
        btnAppleLogin.setOnClickListener { doLogin() }
        tvJoinNow.setOnClickListener {
            etEmail.setText("new_user@skillswap.ru")
            Toast.makeText(requireContext(), "Введите пароль для регистрации", Toast.LENGTH_SHORT).show()
        }
    }
}

class HomeFragment : Fragment(R.layout.fragment_home) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvHomeSkills = view.findViewById<RecyclerView>(R.id.rvHomeSkills)
        val btnSearchBar = view.findViewById<TextView>(R.id.btnSearchBar)
        val btnBannerCreate = view.findViewById<Button>(R.id.btnBannerCreate)

        rvHomeSkills.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.skills.collectLatest { list ->
                rvHomeSkills.adapter = SkillsAdapter(list) { skill ->
                    viewModel.selectedSkill.value = skill
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, SkillDetailsFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        btnSearchBar.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SearchFragment())
                .addToBackStack(null)
                .commit()
        }

        btnBannerCreate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateRequestFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}

class SkillDetailsFragment : Fragment(R.layout.fragment_skill_details) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val skill = viewModel.selectedSkill.value

        skill?.let {
            view.findViewById<TextView>(R.id.tvDetailTitle).text = it.title
            view.findViewById<TextView>(R.id.tvDetailCategory).text = it.category
            view.findViewById<TextView>(R.id.tvDetailMeta).text = "⏱ ${it.duration} • ${it.format}"
            view.findViewById<TextView>(R.id.tvDetailAuthor).text = it.authorName
            view.findViewById<TextView>(R.id.tvDetailInitials).text = it.authorInitials
            view.findViewById<TextView>(R.id.tvDetailStats).text = "★ ${it.rating} (${it.reviewsCount} отзывов) • ${it.swapsCount} обмена"
            view.findViewById<TextView>(R.id.tvDescHeader).text = it.descriptionTitle
            view.findViewById<TextView>(R.id.tvDescBody).text = it.descriptionText
            view.findViewById<TextView>(R.id.tvDetailWantTitle).text = it.wantInReturn
            view.findViewById<TextView>(R.id.tvDetailWantDesc).text = it.wantDetails
            view.findViewById<TextView>(R.id.tvDetailMeeting).text = "📅 ${it.nextMeeting}"
        }

        view.findViewById<TextView>(R.id.btnBackFromDetails).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnProposeSwap).setOnClickListener {
            Toast.makeText(requireContext(), "Предложение обмена отправлено!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChatsFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}

class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvSearchResults = view.findViewById<RecyclerView>(R.id.rvSearchResults)
        val etSearchInput = view.findViewById<EditText>(R.id.etSearchInput)
        val tvFoundCount = view.findViewById<TextView>(R.id.tvFoundCount)
        val btnReset = view.findViewById<TextView>(R.id.btnResetFilters)

        rvSearchResults.layoutManager = LinearLayoutManager(requireContext())

        fun updateList(query: String) {
            val all = viewModel.skills.value
            val filtered = if (query.isBlank()) all else all.filter {
                it.title.contains(query, ignoreCase = true) || it.category.contains(query, ignoreCase = true)
            }
            tvFoundCount.text = "Найдено навыков: ${filtered.size}"
            rvSearchResults.adapter = SkillsAdapter(filtered) { skill ->
                viewModel.selectedSkill.value = skill
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, SkillDetailsFragment())
                    .addToBackStack(null)
                    .commit()
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.skills.collectLatest { updateList(etSearchInput.text.toString()) }
        }

        etSearchInput.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateList(s?.toString() ?: "")
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        btnReset.setOnClickListener { etSearchInput.setText("") }
    }
}

class CreateRequestFragment : Fragment(R.layout.fragment_create_request) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etOffer = view.findViewById<EditText>(R.id.etSkillOffer)
        val etWant = view.findViewById<EditText>(R.id.etSkillWant)
        val etDetails = view.findViewById<EditText>(R.id.etSkillDetails)
        val etWhen = view.findViewById<EditText>(R.id.etWhen)
        val etPlace = view.findViewById<EditText>(R.id.etPlace)

        view.findViewById<TextView>(R.id.btnBackFromCreate).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnPublishRequest).setOnClickListener {
            val title = etOffer.text.toString().ifBlank { "Новый навык" }
            val want = etWant.text.toString().ifBlank { "Любой обмен" }
            val details = etDetails.text.toString()
            viewModel.createNewRequest(title, want, details, "Онлайн", etWhen.text.toString(), etPlace.text.toString())
            Toast.makeText(requireContext(), "Заявка сохранена и опубликована!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }
    }
}

class ChatsFragment : Fragment(R.layout.fragment_chats) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvChats = view.findViewById<RecyclerView>(R.id.rvChats)
        rvChats.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.chats.collectLatest { list ->
                rvChats.adapter = ChatsAdapter(list) { chat ->
                    viewModel.selectedChatPartner.value = chat.partnerName
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ChatRoomFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }
    }
}

class ChatRoomFragment : Fragment(R.layout.fragment_chat_room) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvMessages = view.findViewById<RecyclerView>(R.id.rvMessages)
        val btnBack = view.findViewById<TextView>(R.id.btnBackFromChat)
        val etInput = view.findViewById<EditText>(R.id.etMessageInput)
        val btnSend = view.findViewById<Button>(R.id.btnSendMessage)

        rvMessages.layoutManager = LinearLayoutManager(requireContext())
        btnBack.text = "← ${viewModel.selectedChatPartner.value}"
        btnBack.setOnClickListener { parentFragmentManager.popBackStack() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.messages.collectLatest { list ->
                rvMessages.adapter = MessagesAdapter(list)
                if (list.isNotEmpty()) rvMessages.scrollToPosition(list.size - 1)
            }
        }

        btnSend.setOnClickListener {
            val txt = etInput.text.toString()
            if (txt.isNotBlank()) {
                viewModel.sendMessage(txt)
                etInput.setText("")
            }
        }
    }
}

class SwapsFragment : Fragment(R.layout.fragment_swaps) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rvSwaps = view.findViewById<RecyclerView>(R.id.rvSwaps)
        val btnCreate = view.findViewById<Button>(R.id.btnCreateFromSwaps)

        rvSwaps.layoutManager = LinearLayoutManager(requireContext())

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.swaps.collectLatest { list ->
                rvSwaps.adapter = SwapsAdapter(list) {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, ChatRoomFragment())
                        .addToBackStack(null)
                        .commit()
                }
            }
        }

        btnCreate.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, CreateRequestFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}

class ProfileFragment : Fragment(R.layout.fragment_profile) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tvLatestReview = view.findViewById<TextView>(R.id.tvLatestReview)
        val tvReviewsCount = view.findViewById<TextView>(R.id.tvProfileReviewsCount)

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.reviews.collectLatest { list ->
                tvReviewsCount.text = (7 + list.size).toString()
                if (list.isNotEmpty()) {
                    val r = list.first()
                    tvLatestReview.text = "${r.text}\n★★★★★ ${r.author} • ${r.date}"
                }
            }
        }

        view.findViewById<Button>(R.id.btnLeaveReviewScreen).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ReviewFragment())
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<Button>(R.id.btnLogout).setOnClickListener {
            viewModel.setLoggedIn(false)
            (activity as? MainActivity)?.setBottomNavVisible(false)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, LoginFragment())
                .commit()
        }
    }
}

class ReviewFragment : Fragment(R.layout.fragment_review) {
    private val viewModel: MainViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etReviewText = view.findViewById<EditText>(R.id.etReviewText)

        view.findViewById<TextView>(R.id.btnBackFromReview).setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        view.findViewById<Button>(R.id.btnPublishReview).setOnClickListener {
            val text = etReviewText.text.toString().ifBlank { "Отличный обмен, рекомендую!" }
            viewModel.addReview("Мария Новикова", text, 5)
            Toast.makeText(requireContext(), "Отзыв успешно опубликован!", Toast.LENGTH_SHORT).show()
            parentFragmentManager.popBackStack()
        }
    }
}