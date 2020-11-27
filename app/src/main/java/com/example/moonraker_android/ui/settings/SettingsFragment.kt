package com.example.moonraker_android.ui.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.moonraker_android.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
    }

}
