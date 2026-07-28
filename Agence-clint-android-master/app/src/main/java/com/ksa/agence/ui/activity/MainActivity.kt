package com.ksa.agence.ui.activity

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ksa.agence.R
import com.ksa.agence.app.AgenceApp
import com.ksa.agence.base.BaseActivity
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.common.util.Utilities
import com.ksa.agence.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main
    private lateinit var navHostFragment: NavHostFragment
    var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_main) as NavHostFragment
        navController = navHostFragment.navController
        val navigation: BottomNavigationView = findViewById(R.id.bottomNav)
        val navController = findNavController(R.id.nav_host_main)
        NavigationUI.setupWithNavController(navigation, navController)



        navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    navController.navigate(R.id.menuHome)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.home)
                    mViewDataBinding.tvSearch.visibility = View.VISIBLE
                    mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
                    true
                }

                R.id.menuOffers -> {
                    navController.navigate(R.id.menuOffers)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.offers)
                    mViewDataBinding.tvSearch.visibility = View.VISIBLE
                    mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
                    true
                }

                R.id.menuOrders -> {
                    navController.navigate(R.id.menuOrders)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.orders)
                    mViewDataBinding.tvSearch.visibility = View.VISIBLE
                    mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
                    true
                }

                R.id.menuChat -> {
                    navController.navigate(R.id.menuChat)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.chat)
                    mViewDataBinding.tvSearch.visibility = View.INVISIBLE
                    mViewDataBinding.btnQuickOrder.visibility = View.GONE
                    true
                }

                else -> {
                    true
                }
            }
        }

        if (AgenceApp.pref.authToken != null) {
            Utilities.onLoadImageFromUrl(
                this,
                AgenceApp.pref.loadUserData(this, USER_DATA)!!.data!!.user!!.profile_image,
                mViewDataBinding.ivUser,
            )
        }

        mViewDataBinding.ivUser.setOnClickListener {
            navController.navigate(R.id.settingFragment)
            mViewDataBinding.tvTitleToolBar.setText(R.string.setting)
        }

        mViewDataBinding.ivBackPage.setOnClickListener {
           // navController.popBackStack()
            onBackPressed()
        }

        mViewDataBinding.ivNotification.setOnClickListener {
            navController.navigate(R.id.notificationFragment)
        }

        mViewDataBinding.btnQuickOrder.setOnClickListener {
            navController.navigate(R.id.quickOrderFragment)
        }

        mViewDataBinding.tvSearch.setOnClickListener {
            navController.navigate(R.id.filterFragment)
        }
    }

    private fun initToolBarText() {

        if (navHostFragment.navController.currentDestination!!.id == R.id.menuHome )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.home)
        }
       else if (navHostFragment.navController.currentDestination!!.id == R.id.menuOrders )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.orders)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.menuOffers )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.offers)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.menuChat )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.chat)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.settingFragment )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.setting)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.notificationFragment )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.notification)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.filterFragment)
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.search)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.quickOrderFragment)
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.quick_order)
        }


    }

    fun hideHomeToolbar() {
        mViewDataBinding.bottomNav.visibility = View.GONE
        mViewDataBinding.btnQuickOrder.visibility = View.GONE
        mViewDataBinding.tvSearch.visibility = View.VISIBLE
        mViewDataBinding.fmIvUser.visibility = View.GONE
        mViewDataBinding.ivBackPage.visibility = View.VISIBLE
    }

    fun showHomeToolbar() {
        mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
        mViewDataBinding.tvSearch.visibility = View.VISIBLE
        mViewDataBinding.ivBackPage.visibility = View.GONE
        mViewDataBinding.fmIvUser.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        super.onBackPressed()
        initToolBarText()

    }
    }