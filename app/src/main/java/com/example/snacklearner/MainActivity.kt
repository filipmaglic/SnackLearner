package com.example.snacklearner

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var toolbar: Toolbar
    private lateinit var navigationView: NavigationView
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Zdravi recepti"

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)
        navigationView.setNavigationItemSelectedListener(this)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val toggle = androidx.appcompat.app.ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        toolbar.setNavigationOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        if (savedInstanceState == null) {
            loadSearchFragment()
        }

        // Provjeri role odmah ako je logiran
        val currentUser = auth.currentUser
        if (currentUser != null) {
            firestore.collection("users").document(currentUser.uid).get().addOnSuccessListener { doc ->
                val role = doc.getString("role") ?: "user"
                updateDrawerForRole(role)
            }
        }
    }

    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_home -> loadSearchFragment()
            R.id.nav_saved -> loadSavedRecipesFragment()
            R.id.nav_add_recipe -> loadAddRecipeFragment()
            R.id.nav_settings -> loadSettingsFragment()
        }
        drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }

    fun updateDrawerForRole(role: String) {
        // Ako je admin -> ostavi sve
        if (role == "admin") {
            toolbar.visibility = View.VISIBLE
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        } else {
            toolbar.visibility = View.VISIBLE
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
        }
    }

    private fun loadSearchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SearchFragment())
            .commit()
    }

    private fun loadSettingsFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SettingsFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun loadSavedRecipesFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, SavedRecipesFragment())
            .addToBackStack(null)
            .commit()
    }

    private fun loadAddRecipeFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, AddRecipeFragment())
            .addToBackStack(null)
            .commit()
    }

    fun getDrawerLayout(): DrawerLayout = drawerLayout
    fun getToolbar(): Toolbar = toolbar
}
