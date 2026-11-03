package com.ksa.agence.member.ui.activity

import android.os.Bundle
import android.content.Intent
import com.ksa.agence.common.PushNotifications
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ksa.agence.R
import com.ksa.agence.member.app.AgenceCompanyApp
import com.ksa.agence.member.base.BaseActivity
import com.ksa.agence.common.USER_DATA
import com.ksa.agence.member.common.util.Utilities
import com.ksa.agence.databinding.MemberActivityMainBinding

class MainActivity : BaseActivity<MemberActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.member_activity_main


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

        // Push notifications: ask for permission (Android 13+) and open the
        // notifications page when the app was opened from a notification tap
        PushNotifications.requestPermissionIfNeeded(this)
        PushNotifications.handleOpenIntent(intent, navController)

        // Developer shortcut (debug builds only): switch side or log out quickly
        com.ksa.agence.common.DevTools.attach(this)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            mViewDataBinding.constraintLayout2.visibility =
                if (destination.id == R.id.menuHome
                    || destination.id == R.id.settingFragment
                    || destination.id == R.id.teamProjectFragment
                    || destination.id == R.id.contacUsFragment
                    || destination.id == R.id.menuChat
                    || destination.id == R.id.menuOrders
                    || destination.id == R.id.paymentDistributionFragment
                    || destination.id == R.id.activityLogFragment
                    || destination.id == R.id.teamLeaderPanelFragment
                    || destination.id == R.id.memberPermissionsFragment
                    || destination.id == R.id.opportunityDetailsFragment
                    || destination.id == R.id.allOpportunityFragment
                    || destination.id == R.id.myProfileFragment
                    || destination.id == R.id.allBusinessFragment
                    || destination.id == R.id.addBusinessFragment2
                    || destination.id == R.id.notificationFragment) View.GONE else View.VISIBLE
        }


        navigation.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menuHome -> {
                    navController.navigate(R.id.menuHome)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.home)
                    true
                }

                R.id.menuOrders -> {
                    navController.navigate(R.id.menuOrders)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.orders)
                    true
                }

                R.id.contacUsFragment -> {
                    navController.navigate(R.id.contacUsFragment)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.support)
                    true
                }

                R.id.menuChat -> {
                    navController.navigate(R.id.menuChat)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.member_chat)
                    true
                }

                R.id.settingFragment -> {
                    navController.navigate(R.id.settingFragment)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.member_account)
                    true
                }

                else -> {
                    true
                }
            }
        }


        if (!AgenceCompanyApp.pref.authToken.isNullOrEmpty()) {
            val userData = AgenceCompanyApp.pref.loadUserData(this, USER_DATA)
            val logoUrl = userData?.data?.company?.company_logo
            if (!logoUrl.isNullOrEmpty()) {
                Utilities.onLoadImageFromUrl(
                    this,
                    logoUrl,
                    mViewDataBinding.ivUser
                )
            }
        }
        mViewDataBinding.ivUser.setOnClickListener {
            navController.navigate(R.id.settingFragment)
            mViewDataBinding.tvTitleToolBar.setText(R.string.member_setting)
        }

        mViewDataBinding.ivBackPage.setOnClickListener {
            onBackPressed()
        }
        mViewDataBinding.ivNotification.setOnClickListener {
            navController.navigate(R.id.notificationFragment)
        }

    }

    private fun initToolBarText() {
        if (navHostFragment.navController.currentDestination!!.id == R.id.menuHome ) {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.home)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.menuOrders ) {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.orders)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.contacUsFragment ) {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.support)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.menuChat ) {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.member_chat)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.settingFragment ) {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.member_setting)
        }
        else if (navHostFragment.navController.currentDestination!!.id == R.id.notificationFragment ) {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.member_notifaction)
        }
    }



    fun hideHomeToolbar() {
        mViewDataBinding.bottomNav.visibility = View.GONE
        mViewDataBinding.fmIvUser.visibility = View.GONE
        mViewDataBinding.ivBackPage.visibility = View.VISIBLE
    }

    fun showHomeToolbar() {
        mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mViewDataBinding.ivBackPage.visibility = View.GONE
        mViewDataBinding.fmIvUser.visibility = View.VISIBLE
    }



    override fun onBackPressed() {
        super.onBackPressed()
        initToolBarText()
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        PushNotifications.handleOpenIntent(intent, navController)
    }
}
