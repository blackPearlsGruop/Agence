package com.ksa.agence.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ksa.agence.R
import com.ksa.agence.databinding.ItemNotificationCardBinding
import com.ksa.agence.databinding.ItemNotificationHeaderBinding
import com.ksa.agence.entity.notificationResponse.DataNotificationResponse
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

/**
 * Notifications list shared by the client and member sides.
 * Groups the items into Today and Earlier, highlights unread ones,
 * and picks an icon from the words in the title (the server sends no type).
 */
class NotificationListAdapter(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private sealed class Row {
        data class Header(val title: String) : Row()
        data class Item(val data: DataNotificationResponse) : Row()
    }

    private val rows = ArrayList<Row>()

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1

        private val DATE_PATTERNS = listOf(
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
            "yyyy-MM-dd'T'HH:mm:ss'Z'",
            "yyyy-MM-dd'T'HH:mm:ssXXX",
            "yyyy-MM-dd HH:mm:ss",
            "yyyy-MM-dd"
        )

        /** Returns true when the date is today, false when it is older, null when it can't be read. */
        fun isToday(raw: String?): Boolean? {
            if (raw.isNullOrBlank()) return null
            for (pattern in DATE_PATTERNS) {
                try {
                    val date = SimpleDateFormat(pattern, Locale.US).apply { isLenient = false }.parse(raw)
                        ?: continue
                    val then = Calendar.getInstance().apply { time = date }
                    val now = Calendar.getInstance()
                    return then.get(Calendar.YEAR) == now.get(Calendar.YEAR) &&
                            then.get(Calendar.DAY_OF_YEAR) == now.get(Calendar.DAY_OF_YEAR)
                } catch (e: Exception) {
                    // try the next pattern
                }
            }
            return null
        }

        fun isUnread(item: DataNotificationResponse): Boolean =
            item.is_read == false || (item.is_read == null && item.read_at == null)
    }

    /** Fills the list; headers are added only when the dates can be read. */
    fun submit(list: List<DataNotificationResponse>) {
        rows.clear()
        val today = ArrayList<DataNotificationResponse>()
        val earlier = ArrayList<DataNotificationResponse>()
        val unknown = ArrayList<DataNotificationResponse>()
        list.forEach {
            when (isToday(it.created_at)) {
                true -> today.add(it)
                false -> earlier.add(it)
                null -> unknown.add(it)
            }
        }
        if (unknown.size == list.size) {
            list.forEach { rows.add(Row.Item(it)) }
        } else {
            if (today.isNotEmpty()) {
                rows.add(Row.Header(context.getString(R.string.notif_group_today)))
                today.forEach { rows.add(Row.Item(it)) }
            }
            val rest = earlier + unknown
            if (rest.isNotEmpty()) {
                rows.add(Row.Header(context.getString(R.string.notif_group_earlier)))
                rest.forEach { rows.add(Row.Item(it)) }
            }
        }
        notifyDataSetChanged()
    }

    inner class HeaderHolder(val binding: ItemNotificationHeaderBinding) :
        RecyclerView.ViewHolder(binding.root)

    inner class ItemHolder(val binding: ItemNotificationCardBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun getItemViewType(position: Int): Int =
        if (rows[position] is Row.Header) TYPE_HEADER else TYPE_ITEM

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == TYPE_HEADER) {
            HeaderHolder(ItemNotificationHeaderBinding.inflate(inflater, parent, false))
        } else {
            ItemHolder(ItemNotificationCardBinding.inflate(inflater, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val row = rows[position]) {
            is Row.Header -> (holder as HeaderHolder).binding.tvGroup.text = row.title
            is Row.Item -> bindItem((holder as ItemHolder).binding, row.data)
        }
    }

    private fun bindItem(b: ItemNotificationCardBinding, item: DataNotificationResponse) {
        b.tvTitle.text = item.title.orEmpty()
        b.tvDescription.text = item.description.orEmpty()
        b.tvDescription.visibility = if (item.description.isNullOrBlank()) View.GONE else View.VISIBLE
        b.tvTime.text = item.created_at_for_humans.orEmpty()

        val unread = isUnread(item)
        b.layoutCard.setBackgroundResource(
            if (unread) R.drawable.bg_notif_card_unread else R.drawable.bg_notif_card
        )
        b.viewUnreadDot.visibility = if (unread) View.VISIBLE else View.INVISIBLE

        val (icon, tile) = iconFor("${item.title} ${item.description}")
        b.ivIcon.setImageResource(icon)
        b.ivIcon.setBackgroundResource(tile)
    }

    /** Chooses an icon and tile color from keywords, in Arabic and English. */
    private fun iconFor(text: String): Pair<Int, Int> {
        val t = text.lowercase(Locale.ROOT)
        fun has(vararg words: String) = words.any { t.contains(it) }
        return when {
            has("رسالة", "رسائل", "محادثة", "message", "chat") ->
                R.drawable.ic_notif_chat to R.drawable.bg_notif_tile_orange
            has("تقييم", "قيّم", "قيم ", "rating", "review", "rate") ->
                R.drawable.ic_notif_star to R.drawable.bg_notif_tile_orange
            has("دفع", "مبلغ", "محفظة", "إيداع", "ايداع", "سحب", "ريال", "فاتورة", "payment", "wallet", "invoice", "paid") ->
                R.drawable.ic_notif_wallet to R.drawable.bg_notif_tile_blue
            has("قبول", "تسليم", "سلّم", "اكتمل", "accepted", "delivered", "completed", "approved") ->
                R.drawable.ic_notif_check to R.drawable.bg_notif_tile_blue
            has("عرض", "فرصة", "offer", "opportunit") ->
                R.drawable.ic_notif_offer to R.drawable.bg_notif_tile_orange
            has("طلب", "order", "request", "مشروع", "project") ->
                R.drawable.ic_notif_order to R.drawable.bg_notif_tile_blue
            else -> R.drawable.ic_notif_bell to R.drawable.bg_notif_tile_blue
        }
    }

    override fun getItemCount(): Int = rows.size
}
