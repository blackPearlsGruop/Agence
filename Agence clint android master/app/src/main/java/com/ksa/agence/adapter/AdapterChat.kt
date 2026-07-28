package com.ksa.agence.adapter

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.entity.Message
import com.yehia.wave.views.WavePlayerView
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class AdapterChat(
    private val context: Activity, private val messages: List<Message>
) : RecyclerView.Adapter<AdapterChat.ViewHolder>() {

    companion object {
        private const val LEFT_RECEIVE = 0
        private const val RIGHT_SEND = 1
    }

    private var mediaPlayer: MediaPlayer? = null
    private val handler = Handler(Looper.getMainLooper())
    private var seekBarUpdateRunnable: Runnable? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = if (viewType == RIGHT_SEND) {
            LayoutInflater.from(context).inflate(R.layout.item_message_sent, parent, false)
        } else {
            LayoutInflater.from(context).inflate(R.layout.item_message_received, parent, false)
        }
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val chatItem = messages[position]
        holder.itemChatTVMessage.text = chatItem.messageText

        when (chatItem.messageType) {
            "TEXT" -> {
                holder.constraintLayoutText?.visibility = View.VISIBLE
                holder.constraintLayoutImage?.visibility = View.GONE
                holder.audioPlayerLayout?.visibility = View.GONE
            }

            "IMAGE" -> {
                holder.constraintLayoutText?.visibility = View.GONE
                holder.constraintLayoutImage?.visibility = View.VISIBLE
                holder.audioPlayerLayout?.visibility = View.GONE

                holder.progressBar?.visibility = View.VISIBLE
                Glide.with(context).load(chatItem.messageImage)
                    .apply(RequestOptions().override(Target.SIZE_ORIGINAL))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBar?.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable,
                            model: Any,
                            target: Target<Drawable>?,
                            dataSource: DataSource,
                            isFirstResource: Boolean
                        ): Boolean {
                            holder.progressBar?.visibility = View.GONE
                            return false
                        }
                    }).into(holder.imageMessage!!)
            }

            "AUDIO" -> {
                holder.constraintLayoutText?.visibility = View.GONE
                holder.constraintLayoutImage?.visibility = View.GONE
                holder.audioPlayerLayout?.visibility = View.VISIBLE

                holder.recordPlays?.setAudioTarget(
                    chatItem.messageVoice, context, chatItem.timestamp.toString()
                )
            }
        }

        holder.itemStatus?.let {
            if (chatItem.isSent && chatItem.isReceived) {
                it.setImageResource(R.drawable.icon_show_message)
            } else if (chatItem.isSent && !chatItem.isReceived) {
                it.setImageResource(R.drawable.ic_check_single)
            } else {
                it.visibility = View.GONE
            }
        }
    }

    override fun getItemCount(): Int = messages.size

    override fun getItemViewType(position: Int): Int {
        return if (messages[position].senderId == AgenceApp.pref.loadUserData(
                context, USER_DATA
            )!!.data!!.user!!.id!!
        ) {
            RIGHT_SEND
        } else {
            LEFT_RECEIVE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemChatTVMessage: TextView = itemView.findViewById(R.id.item_chat_tv_message)
        val itemStatus: ImageView? = itemView.findViewById(R.id.item_chat_iv_status)
        val constraintLayoutImage: CardView? = itemView.findViewById(R.id.constraintLayoutImage)
        val constraintLayoutText: LinearLayout? = itemView.findViewById(R.id.constraintLayoutText)
        val audioPlayerLayout: LinearLayout? = itemView.findViewById(R.id.audioPlayerLayout)

        val recordPlays: WavePlayerView? = itemView.findViewById(R.id.record_plays)
        val imageMessage: ImageView? = itemView.findViewById(R.id.iv_image)
        val progressBar: ProgressBar? = itemView.findViewById(R.id.progressBar)
    }

    override fun onViewRecycled(holder: ViewHolder) {
        super.onViewRecycled(holder)
        if (mediaPlayer != null) {
            mediaPlayer?.release()
            mediaPlayer = null
            handler.removeCallbacks(seekBarUpdateRunnable!!)
        }
    }
}
