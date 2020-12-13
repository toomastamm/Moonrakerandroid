package com.example.moonraker_android

import android.content.Intent
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
import com.example.moonraker_android.api.MoonrakerService
import com.example.moonraker_android.ui.settings.SettingsActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.json.responseJson
import com.github.kittinunf.result.Result
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private val requestSettings = 100
    private lateinit var appBarConfiguration: AppBarConfiguration

    // Printer objects

    // https://github.com/Arksine/moonraker/blob/master/docs/printer_objects.md#heater_bed
    var heaterBeds: MutableList<String> = ArrayList()

    // https://github.com/Arksine/moonraker/blob/master/docs/printer_objects.md#extruder
    var extruders: MutableList<String> = ArrayList()

    // https://github.com/Arksine/moonraker/blob/master/docs/printer_objects.md#toolhead
    var toolhead: String? = null

    var latestUpdate: JSONObject? = null

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

        MoonrakerService.init(this)

        // Connect to moonraker
        connectToMoonraker()
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

    private fun connectToMoonraker(): Job {
        val httpAsync = ("/printer/info").httpGet()
            .also { Log.d("PrinterConnect", "Connecting to ${it.url}") }
            .responseJson { req, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e("firstGet", ex.toString())

                        val toast = Toast.makeText(
                            applicationContext,
                            "Could not connect to ${req.url}",
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
                        getPrinterObjects()
                    }
                }
            }

        return MainScope().launch(Dispatchers.IO) {
            httpAsync.join()
        }
    }

    private fun getPrinterObjects() {
        val httpAsync = ("/printer/objects/list").httpGet()
            .also { Log.d("GetPrinterObjects", "Connecting to ${it.url}") }
            .responseJson() { _, _, result ->
                when (result) {
                    is Result.Failure -> {
                        val ex = result.getException()
                        Log.e("GetPrinterObjects", ex.toString())
                    }
                    is Result.Success -> {
                        val data = result.get().obj()
                        Log.d(
                            "PrinterObjects",
                            data.getJSONObject("result").getJSONArray("objects").toString()
                        )

                        val arr = data.getJSONObject("result").getJSONArray("objects")

                        for (i in 0 until arr.length()) {
                            val obj = arr[i]

                            if (obj.toString().startsWith("heater_bed")) {
                                heaterBeds.add(obj.toString())
                            } else if (obj.toString().startsWith("extruder")) {
                                extruders.add(obj.toString())
                            } else if (obj.toString().startsWith("toolhead")) {
                                toolhead = obj.toString()
                            }
                        }
                    }
                }
            }

        httpAsync.join()

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
