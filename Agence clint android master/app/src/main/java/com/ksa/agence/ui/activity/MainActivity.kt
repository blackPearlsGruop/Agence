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
                    // FIGMA REDESIGN: the new fragment_home.xml header (logo, greeting,
                    // search bar, filter) replaces this old shared toolbar entirely, so
                    // hide it here to avoid showing both stacked on top of each other.
                    mViewDataBinding.constraintLayout2.visibility = View.GONE
                    mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
                    true
                }

                R.id.menuOffers -> {
                    navController.navigate(R.id.menuOffers)
                    mViewDataBinding.constraintLayout2.visibility = View.VISIBLE
                    mViewDataBinding.tvTitleToolBar.setText(R.string.offers)
                    mViewDataBinding.tvSearch.visibility = View.VISIBLE
                    mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
                    true
                }

                R.id.menuOrders -> {
                    navController.navigate(R.id.menuOrders)
                    // FIGMA REDESIGN: fragment_orders.xml has its own light header now,
                    // same pattern as Home/Account/Messages/Support.
                    mViewDataBinding.constraintLayout2.visibility = View.GONE
                    mViewDataBinding.btnQuickOrder.visibility = View.VISIBLE
                    true
                }

                R.id.menuChat -> {
                    navController.navigate(R.id.menuChat)
                    // FIGMA REDESIGN: fragment_chat.xml has its own light header now,
                    // same pattern as Home/Account/Orders — hide the old shared blue
                    // toolbar instead of leaving its search box reserved-but-invisible.
                    mViewDataBinding.constraintLayout2.visibility = View.GONE
                    mViewDataBinding.btnQuickOrder.visibility = View.GONE
                    true
                }

                R.id.contacUsFragment -> {
                    navController.navigate(R.id.contacUsFragment)
                    // FIGMA REDESIGN: fragment_contac_us.xml has its own light header now,
                    // same pattern as Home/Account/Orders/Messages.
                    mViewDataBinding.constraintLayout2.visibility = View.GONE
                    mViewDataBinding.btnQuickOrder.visibility = View.GONE
                    true
                }

                R.id.settingFragment -> {
                    navController.navigate(R.id.settingFragment)
                    // FIGMA REDESIGN: fragment_setting.xml has its own light header
                    // (bell + title), same pattern as Home — hide the old shared
                    // blue toolbar entirely instead of leaving its search box
                    // reserved-but-invisible (that reserved space was the big blue
                    // empty block under the title).
                    mViewDataBinding.constraintLayout2.visibility = View.GONE
                    mViewDataBinding.btnQuickOrder.visibility = View.GONE
                    true
                }

                else -> {
                    true
                }
            }
        }

        // Home is the default tab on launch — hide the old toolbar from the start too,
        // since the listener above only fires on a later tap, not the initial state.
        mViewDataBinding.constraintLayout2.visibility = View.GONE

        if (!AgenceApp.pref.authToken.isNullOrEmpty()) {
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