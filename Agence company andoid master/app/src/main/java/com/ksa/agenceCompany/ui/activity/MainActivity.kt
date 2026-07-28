package com.ksa.agenceCompany.ui.activity

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ksa.agenceCompany.R
import com.ksa.agenceCompany.AgenceCompanyApp
import com.ksa.agenceCompany.base.BaseActivity
import com.ksa.agenceCompany.common.USER_DATA
import com.ksa.agenceCompany.common.util.Utilities
import com.ksa.agenceCompany.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int = R.layout.activity_main


    private lateinit var navHostFragment: NavHostFragment

    // lateinit var type: String
    var navController: NavController? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //   enableEdgeToEdge()
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

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

                    true
                }

                R.id.menuOrders -> {
                    navController.navigate(R.id.menuOrders)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.orders)
                    // mViewDataBinding.ivUser.visibility=View.VISIBLE

                    true
                }

                R.id.menuWallet -> {
                    navController.navigate(R.id.menuWallet)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.wallet)
                    //   mViewDataBinding.ivUser.visibility=View.VISIBLE


                    true
                }



                R.id.menuChat -> {
                    navController.navigate(R.id.menuChat)
                    mViewDataBinding.tvTitleToolBar.setText(R.string.chat)
                    //   mViewDataBinding.ivUser.visibility=View.VISIBLE

                    true
                }

                else -> {
                    true

                }
            }


        }






        if (AgenceCompanyApp.pref.authToken != null) {
            Utilities.onLoadImageFromUrl(
                this,
                AgenceCompanyApp.pref.loadUserData(this, USER_DATA)!!.data!!.company!!.company_logo,
                mViewDataBinding.ivUser
            )

        }
        mViewDataBinding.ivUser.setOnClickListener {
            navController.navigate(R.id.settingFragment)
            mViewDataBinding.tvTitleToolBar.setText(R.string.setting)

        }

        mViewDataBinding.ivBackPage.setOnClickListener {
           // navController.popBackStack()
            //showHomeToolbar()
            onBackPressed()
        }
        mViewDataBinding.ivNotification.setOnClickListener {
            navController.navigate(R.id.notificationFragment)
        }
        mViewDataBinding.btnAdd.setOnClickListener {
            navController.navigate(R.id.addAServiceOrOfferFragment)
            mViewDataBinding.tvTitleToolBar.setText(R.string.add_a_service_or_offer)

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
        else if (navHostFragment.navController.currentDestination!!.id == R.id.menuWallet )
        {
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.wallet)
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
            mViewDataBinding.tvTitleToolBar.text=getString(R.string.notifaction)
        }



    }



    fun hideHomeToolbar() {
        mViewDataBinding.bottomNav.visibility = View.GONE
        mViewDataBinding.btnAdd.visibility = View.GONE
//        mViewDataBinding.tvSearch.visibility = View.VISIBLE
        mViewDataBinding.fmIvUser.visibility = View.GONE
        mViewDataBinding.ivBackPage.visibility = View.VISIBLE


    }

    fun showHomeToolbar() {
        mViewDataBinding.bottomNav.visibility = View.VISIBLE
        mViewDataBinding.btnAdd.visibility = View.VISIBLE
//        mViewDataBinding.tvSearch.visibility = View.VISIBLE
        mViewDataBinding.ivBackPage.visibility = View.GONE
        mViewDataBinding.fmIvUser.visibility = View.VISIBLE

    }



    override fun onBackPressed() {
        super.onBackPressed()
        initToolBarText()

    }

}