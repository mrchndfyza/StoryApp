package com.greentea.storyapp2.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.greentea.storyapp2.R
import com.greentea.storyapp2.databinding.ActivityHomeBinding
import com.greentea.storyapp2.utils.Constants
import com.greentea.storyapp2.viewmodel.preferences.UserPreference

class HomeActivity : AppCompatActivity() {
    //BINDING
    private lateinit var homeBinding: ActivityHomeBinding

    private lateinit var bottomNavigationView: BottomNavigationView

    private lateinit var userPreference: UserPreference
    private var token = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)

        supportActionBar?.title = "Story"

        //BOTTOM NAV SETTINGS
        homeBinding.bnvComponents.background = null
        homeBinding.bnvComponents.menu.getItem(1).isEnabled = false

        //CONNECT BOTTOM NAVIGATION TO THIS ACTIVITY
        bottomNavigationView = homeBinding.bnvComponents
        val navController = findNavController(R.id.fm_nav_host_story)
        bottomNavigationView.setupWithNavController(navController)

        homeBinding.fabAdd.setOnClickListener {
            startActivity(Intent(this@HomeActivity, AddStoryActivity::class.java))
        }

        userPreference = UserPreference(this)
        val tokenFromPreferences = userPreference.getDataLogin(Constants.TOKEN)
        token = "Bearer $tokenFromPreferences"
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.map ->{
                val intent = Intent(this@HomeActivity, MapsActivity::class.java)
                startActivity(intent)
                true
            }R.id.list ->{
                val intent = Intent(this@HomeActivity, PagingActivity::class.java)
                startActivity(intent)
                true}
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }

}