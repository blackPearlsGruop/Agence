package com.ksa.agence.base

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
import androidx.fragment.app.Fragment
import com.ksa.agence.interfaces.ConnectivityListener
import com.ksa.agence.common.util.NetworkReceiver

abstract class BaseFragment<T : ViewDataBinding> : Fragment(), ConnectivityListener {
    lateinit var mViewDataBinding: T
    protected var activity: BaseActivity<*>? = null

    private lateinit var networkReceiver: NetworkReceiver


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is BaseActivity<*>) {
            this.activity = context
        }
    }

    override fun onDetach() {
        activity = null
        super.onDetach()
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        mViewDataBinding = DataBindingUtil.inflate(
            inflater, getLayoutId(), container, false
        )
        //   mViewDatamViewDataBinding.lifecycleOwner = viewLifecycleOwner
        networkReceiver = NetworkReceiver(this)
        registerNetworkReceiver()
        return mViewDataBinding.root
    }


    fun showProgress(it: Boolean) {
        if (it) showProgressDialog()
        else hideProgressDialog()

    }

    fun showProgressDialog() {
        activity?.showProgressDialog()
    }

    fun hideProgressDialog() {
        activity?.hideProgressDialog()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
        activity?.openActivity(destinationActivity, bundle)
    }

    fun openActivityAndFinish(destinationActivity: Class<*>, bundle: Bundle? = null) {
        activity?.openActivityAndFinish(destinationActivity, bundle)

    }

    fun hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

//    fun onBack() {
//        view?.let { activity?.onBackPressed() }
//    }

    //Clean after destroy (memory leak)
    override fun onDestroy() {
        super.onDestroy()


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

//abstract class BaseFragment<T : ViewDataBinding> : Fragment(), ConnectivityListener {
//
//
//       private lateinit var networkReceiver: NetworkReceiver
//
//
//    private var _binding: T? = null
//    val mViewDataBinding get() = _binding!!
//
//    //    lateinit var mViewDataBinding: T
//    protected var activity: BaseActivity<*>? = null
//
//
//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        if (context is BaseActivity<*>) {
//            this.activity = context
//        }
//    }
//
//    override fun onDetach() {
//        activity = null
//        super.onDetach()
//    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
//    ): View? {
//        if (_binding == null) {
//            _binding = DataBindingUtil.inflate(
//                inflater, getLayoutId(), container, false
//            )
//            whenBindingInit()
//
//            networkReceiver = NetworkReceiver(this)
//            registerNetworkReceiver()
//
//        }
//
//        whenBindingReInit()
//        return mViewDataBinding.root
//    }
//
//    //Use this to mack function at first fragment open
//    private fun whenBindingInit() {
//
//    }
//
//    //Use this to mack function if you need to start every time
//    private fun whenBindingReInit() {
//
//    }
//
//    fun showProgress(it: Boolean) {
//        if (it) showProgressDialog()
//        else hideProgressDialog()
//
//    }
//
//    fun showProgressDialog() {
//        activity?.showProgressDialog()
//    }
//
//    fun hideProgressDialog() {
//        activity?.hideProgressDialog()
//    }
//
//    @LayoutRes
//    abstract fun getLayoutId(): Int
//
//    fun openActivity(destinationActivity: Class<*>, bundle: Bundle? = null) {
//        activity?.openActivity(destinationActivity, bundle)
//    }
//
//    fun openActivityAndFinish(destinationActivity: Class<*>, bundle: Bundle? = null) {
//        activity?.openActivityAndFinish(destinationActivity, bundle)
//
//    }
//
//    fun hideKeyboard() {
//        view?.let { activity?.hideKeyboard(it) }
//    }
//
//    //Clean after destroy (memory leak)
//    override fun onDestroy() {
//        super.onDestroy()
//        _binding = null
//        unregisterNetworkReceiver()
//
//    }
//
//
//    private fun unregisterNetworkReceiver() {
//        activity?.applicationContext?.let { context ->
//            context.unregisterReceiver(networkReceiver)
//        }
//    }
//
//    private fun registerNetworkReceiver() {
//        activity?.applicationContext?.let { context ->
//            val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
//            context.registerReceiver(networkReceiver, filter)
//        }
//    }
//
//}