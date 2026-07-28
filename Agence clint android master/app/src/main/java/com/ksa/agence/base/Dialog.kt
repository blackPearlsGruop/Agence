package com.ksa.agence.base

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.ksa.agence.R

abstract class Dialog<T : ViewDataBinding> : DialogFragment() {

    lateinit var mViewDataBinding: T
    protected var activity: BaseActivity<*>? = null

    protected lateinit var progressDialog: Dialog

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this.activity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)

        val window = dialog!!.window
        window!!.setLayout(
            ConstraintLayout.LayoutParams.MATCH_PARENT, ConstraintLayout.LayoutParams.WRAP_CONTENT
        )


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

}