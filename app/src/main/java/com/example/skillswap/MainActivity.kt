package com.example.skillswap

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottom_navigation)

        if (savedInstanceState == null) {
            val loggedIn = viewModel.isLoggedIn()
            setBottomNavVisible(loggedIn)
            val startFragment = if (loggedIn) HomeFragment() else LoginFragment()
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, startFragment)
                .commit()
        }

        bottomNav.setOnItemSelectedListener { item ->
            val fragment = when (item.itemId) {
                R.id.nav_home -> HomeFragment()
                R.id.nav_search -> SearchFragment()
                R.id.nav_swaps -> SwapsFragment()
                R.id.nav_chats -> ChatsFragment()
                R.id.nav_profile -> ProfileFragment()
                else -> HomeFragment()
            }
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit()
            true
        }
    }

    fun setBottomNavVisible(visible: Boolean) {
        bottomNav.visibility = if (visible) View.VISIBLE else View.GONE
    }
}