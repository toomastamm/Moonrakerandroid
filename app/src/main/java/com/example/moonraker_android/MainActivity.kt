package com.example.moonraker_android

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.example.moonraker_android.ui.settings.SettingsActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private val requestSettings = 100
    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        val navView: NavigationView = findViewById(R.id.nav_view)
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_print_status,
                R.id.nav_temperatures,
                R.id.nav_motors,
                R.id.nav_sd_card
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        // Set up preferences
        var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        PreferenceManager.setDefaultValues(
            this,
            R.xml.preferences, false
        );

        // Connect to moonraker
        connectToMoonraker(prefs)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_settings) {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivityForResult(intent, requestSettings);
        }
        return super.onOptionsItemSelected(item);
    }

    private fun connectToMoonraker(prefs: SharedPreferences?): Job? {
        if (prefs == null) {
            return null
        }

        Log.d(
            "PrinterConnect", "Connecting to http://${
                prefs.getString(
                    "moonraker_ip",
                    "undefined"
                )
            }:${prefs.getString("moonraker_port", "undefined")}"
        )

        val httpAsync = (
                "http://" +
                        prefs.getString("moonraker_ip", "undefined") + ":" +
                        prefs.getString("moonraker_port", "undefined") +
                        "/printer/info"
                ).httpGet()
            .responseJson() { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e("firstGet", ex.toString())

                        val toast = Toast.makeText(
                            applicationContext,
                            "Could not connect to http://${
                                prefs.getString(
                                    "moonraker_ip",
                                    "undefined"
                                )
                            }:${prefs.getString("moonraker_port", "undefined")}",
                            Toast.LENGTH_LONG
                        )
                        toast.show()
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        val toast = Toast.makeText(
                            applicationContext,
                            data.getJSONObject("result").getString("state_message"),
                            Toast.LENGTH_SHORT
                        )
                        toast.show()
                    }
                }
            }

        return MainScope().launch(Dispatchers.IO) {
            httpAsync.join()
        }
    }

    // Recreates activity on settings change
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            requestSettings -> {
                Log.d("activityResult", "recreating")
                // Recreate activity
                recreate()
            }
        }
    }

}
