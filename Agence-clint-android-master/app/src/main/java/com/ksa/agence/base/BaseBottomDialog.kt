package com.ksa.agence.base

import android.app.AlertDialog
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ksa.agence.R
import com.ksa.agence.common.util.NetworkReceiver
import com.ksa.agence.interfaces.ConnectivityListener

abstract class BaseBottomDialog<T : ViewDataBinding> : BottomSheetDialogFragment(),
    ConnectivityListener {

    lateinit var mViewDataBinding: T
    protected var activity: BaseActivity<*>? = null


    protected lateinit var progressDialog: AlertDialog
    private lateinit var networkReceiver: NetworkReceiver



    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this.activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding =
            DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        networkReceiver = NetworkReceiver(this)
        registerNetworkReceiver()

        return mViewDataBinding.root
    }


    @LayoutRes
    abstract fun getLayoutId(): Int

    override fun onDetach() {
        activity = null
        super.onDetach()
    }

    fun showProgress(it: Boolean) {
        if (it) showProgressDialog()
        else hideProgressDialog()

    }

    private fun showProgressDialog() {
        hideProgressDialog()
        val alertDialogBuilder = AlertDialog.Builder(requireActivity()).setCancelable(false)
        alertDialogBuilder.setView(R.layout.loader)
        progressDialog = alertDialogBuilder.create()
        progressDialog.window?.setBackgroundDrawableResource(R.color.transparent)
        progressDialog.show()
    }


    fun openActivityAndFinish(destinationActivity: Class<*>) {
        activity?.openActivityAndFinish(destinationActivity)
    }

    fun hideProgressDialog() {
        if (::progressDialog.isInitialized && progressDialog.isShowing) progressDialog.dismiss()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        unregisterNetworkReceiver()
    }

    private fun unregisterNetworkReceiver() {
        activity?.applicationContext?.let { context ->
            context.unregisterReceiver(networkReceiver)
        }
    }

    private fun registerNetworkReceiver() {
        activity?.applicationContext?.let { context ->
            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            context.registerReceiver(networkReceiver, filter)
        }
    }

}

