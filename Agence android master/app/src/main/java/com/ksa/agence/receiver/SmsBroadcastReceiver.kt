package com.ksa.agence.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.Status


class SmsBroadcastReceiver :  BroadcastReceiver() {


    var smsBroadcastReceiveListener: SmsBroadcastReceiveListener? = null

    override fun onReceive(context: Context?, intent: Intent?) {

        if (SmsRetriever.SMS_RETRIEVED_ACTION==intent?.action) {

            var extras=intent.extras
            val smsRetrieverStatus=extras?.get(SmsRetriever.EXTRA_STATUS) as Status
            when (smsRetrieverStatus.statusCode)
            {
                CommonStatusCodes.SUCCESS -> {
                    val messageIntent = extras.getParcelable<Intent>(SmsRetriever.EXTRA_CONSENT_INTENT)

                    smsBroadcastReceiveListener?.onSuccess(messageIntent)

                }
                CommonStatusCodes.TIMEOUT->{
                    smsBroadcastReceiveListener?.onFailure()

                }
            }
            }

        }

    interface SmsBroadcastReceiveListener {
        fun onSuccess(intent: Intent?)
        fun onFailure()

    }
}