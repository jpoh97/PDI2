package co.edu.udea.pdi2;

/**
 * Created by jpoh97 on 23/03/18.
 */

import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle bundle, String s) {
        // Load the Preferences from the XML file
        addPreferencesFromResource(R.xml.app_preferences);

        ListPreference listPreference = (ListPreference) findPreference("key1");

        if(listPreference.getValue() == null) {
           listPreference.setValueIndex(0);
        }

        listPreference.setSummary(listPreference.getValue().toString().toUpperCase());
        listPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                preference.setSummary(newValue.toString().toUpperCase());
                return true;
            }
        });
    }
}
