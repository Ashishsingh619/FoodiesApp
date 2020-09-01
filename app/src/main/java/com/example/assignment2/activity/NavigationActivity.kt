package com.example.assignment2.activity

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.MenuItem
import android.widget.FrameLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.assignment2.R
import com.example.assignment2.fragments.*
import com.google.android.material.navigation.NavigationView

class NavigationActivity : AppCompatActivity() {
    lateinit var toolbar: Toolbar
    lateinit var drawerLayout: DrawerLayout
    lateinit var Navigationview: NavigationView
    lateinit var Frame: FrameLayout
    lateinit var sharedpreference: SharedPreferences
    var previousMenu: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_navigation)
        sharedpreference =
            getSharedPreferences(getString(R.string.shared_file_name), Context.MODE_PRIVATE)
        toolbar = findViewById(R.id.toolbar)
        drawerLayout = findViewById(R.id.drawer)
        Navigationview = findViewById(R.id.navigation)
        Frame = findViewById(R.id.frame)
        setAction()
        HomeFragments()
        val actionDrawer = ActionBarDrawerToggle(
            this@NavigationActivity,
            drawerLayout,
            toolbar,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionDrawer)
        actionDrawer.syncState()
        Navigationview.setNavigationItemSelectedListener {
            if (previousMenu != null)
                previousMenu?.isChecked = false
            it.isChecked = true
            it.isCheckable = true
            previousMenu = it
            if (it.itemId == R.id.itm_Home) {
                HomeFragments()
            }
            if (it.itemId == R.id.itm_profile) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    ProfileFragment()
                )
                    .commit()
                drawerLayout.closeDrawers()
                supportActionBar?.title = "My Profile"
            }
            if (it.itemId == R.id.itm_fav) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    FavoriteFragments()
                )
                    .commit()
                drawerLayout.closeDrawers()
                supportActionBar?.title = "Favorite Restaurants"
            }
            if (it.itemId == R.id.itm_his) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    HistoryFragment()
                )
                    .commit()
                drawerLayout.closeDrawers()
                supportActionBar?.title = "My Previous Orders"
            }
            if (it.itemId == R.id.itm_Faq) {
                supportFragmentManager.beginTransaction().replace(
                    R.id.frame,
                    FaqFragment()
                )
                    .commit()
                drawerLayout.closeDrawers()
                supportActionBar?.title = "Frequently Asked Questions"
            }
            if (it.itemId == R.id.itm_logOut) {
                drawerLayout.closeDrawers()
                val dialog = AlertDialog.Builder(this@NavigationActivity)
                dialog.setTitle("Confirmation")
                dialog.setMessage("Are you sure you want to Log Out?")
                dialog.setPositiveButton("No") { text, Listener ->
                }
                dialog.setNegativeButton("Yes") { text, Listener ->
                    sharedpreference.edit().putBoolean("LoggedIn", false).apply()
                    ActivityCompat.finishAffinity(this@NavigationActivity)
                    val intent = Intent(this@NavigationActivity, LoginActivity::class.java)
                    startActivity(intent)


                }
                dialog.create()
                dialog.show()
            }
            return@setNavigationItemSelectedListener true
        }

    }

    fun setAction() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar"
        supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onBackPressed() {
        var fragment = supportFragmentManager.findFragmentById(R.id.frame)
        if (fragment !== HomeFragment()) {
            HomeFragments()
        } else
            super.onBackPressed()
    }

    fun HomeFragments() {
        supportFragmentManager.beginTransaction().replace(
            R.id.frame,
            HomeFragment()
        )
            .commit()
        drawerLayout.closeDrawers()
        supportActionBar?.title = "All Restaurants"
        Navigationview.setCheckedItem(R.id.itm_Home)
    }

}
