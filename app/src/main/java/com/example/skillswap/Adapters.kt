package com.example.skillswap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SkillsAdapter(
    private val skills: List<SkillItem>,
    private val onClick: (SkillItem) -> Unit
) : RecyclerView.Adapter<SkillsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvSkillTitle: TextView = view.findViewById(R.id.tvSkillTitle)
        val tvSkillMeta: TextView = view.findViewById(R.id.tvSkillMeta)
        val tvWantInReturn: TextView = view.findViewById(R.id.tvWantInReturn)
        val tvAuthorName: TextView = view.findViewById(R.id.tvAuthorName)
        val tvAuthorInitials: TextView = view.findViewById(R.id.tvAuthorInitials)
        val tvRating: TextView = view.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skill, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val skill = skills[position]
        holder.tvSkillTitle.text = skill.title
        holder.tvSkillMeta.text = "${skill.category} • ${skill.format} • ${skill.duration}"
        holder.tvWantInReturn.text = "⇄ ${skill.wantInReturn}"
        holder.tvAuthorName.text = skill.authorName
        holder.tvAuthorInitials.text = skill.authorInitials
        holder.tvRating.text = "☆ ${skill.rating}"
        holder.itemView.setOnClickListener { onClick(skill) }
    }

    override fun getItemCount() = skills.size
}

class SwapsAdapter(
    private val swaps: List<SwapRequest>,
    private val onClick: (SwapRequest) -> Unit
) : RecyclerView.Adapter<SwapsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById(R.id.tvSkillTitle)
        val tvMeta: TextView = view.findViewById(R.id.tvSkillMeta)
        val tvWant: TextView = view.findViewById(R.id.tvWantInReturn)
        val tvAuthor: TextView = view.findViewById(R.id.tvAuthorName)
        val tvInitials: TextView = view.findViewById(R.id.tvAuthorInitials)
        val tvStatus: TextView = view.findViewById(R.id.tvRating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_skill, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val swap = swaps[position]
        holder.tvTitle.text = swap.skillTitle
        holder.tvMeta.text = swap.dateInfo
        holder.tvWant.text = "⇄ ${swap.inReturnTitle}"
        holder.tvAuthor.text = swap.partnerName
        holder.tvInitials.text = swap.partnerInitials
        holder.tvStatus.text = swap.status
        holder.itemView.setOnClickListener { onClick(swap) }
    }

    override fun getItemCount() = swaps.size
}

class ChatsAdapter(
    private val chats: List<ChatThread>,
    private val onClick: (ChatThread) -> Unit
) : RecyclerView.Adapter<ChatsAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvChatPartner: TextView = view.findViewById(R.id.tvChatPartner)
        val tvChatInitials: TextView = view.findViewById(R.id.tvChatInitials)
        val tvChatTopic: TextView = view.findViewById(R.id.tvChatTopic)
        val tvChatLastMsg: TextView = view.findViewById(R.id.tvChatLastMsg)
        val tvChatTime: TextView = view.findViewById(R.id.tvChatTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_chat, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chat = chats[position]
        holder.tvChatPartner.text = chat.partnerName
        holder.tvChatInitials.text = chat.partnerInitials
        holder.tvChatTopic.text = chat.skillTopic
        holder.tvChatLastMsg.text = chat.lastMessage
        holder.tvChatTime.text = chat.time
        holder.itemView.setOnClickListener { onClick(chat) }
    }

    override fun getItemCount() = chats.size
}

class MessagesAdapter(private val messages: List<ChatMessage>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ME = 1
    private val VIEW_TYPE_PARTNER = 2

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].isFromMe) VIEW_TYPE_ME else VIEW_TYPE_PARTNER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = if (viewType == VIEW_TYPE_ME) R.layout.item_message_me else R.layout.item_message_partner
        val view = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        return object : RecyclerView.ViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val msg = messages[position]
        if (holder.itemViewType == VIEW_TYPE_ME) {
            holder.itemView.findViewById<TextView>(R.id.tvMyMsg).text = msg.text
        } else {
            holder.itemView.findViewById<TextView>(R.id.tvPartnerMsg).text = msg.text
        }
    }

    override fun getItemCount() = messages.size
}