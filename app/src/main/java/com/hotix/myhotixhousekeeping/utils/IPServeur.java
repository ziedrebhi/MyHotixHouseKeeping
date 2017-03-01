package com.hotix.myhotixhousekeeping.utils;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.SwitchPreference;

import com.hotix.myhotixhousekeeping.R;

public class IPServeur extends PreferenceActivity implements OnSharedPreferenceChangeListener {

    SharedPreferences pref;
    private EditTextPreference ipPreference;
    private SwitchPreference imagePreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.prefserveur);

        pref = getPreferenceManager().getSharedPreferences();
        pref.registerOnSharedPreferenceChangeListener(this);
        ipPreference = (EditTextPreference) getPreferenceScreen().findPreference("serveur");
        imagePreference = (SwitchPreference) getPreferenceScreen().findPreference("image");

        String serveur = (pref.getString("serveur", ""));
        ipPreference.setSummary(serveur);


        Boolean isimage = pref.getBoolean("image", false);
        imagePreference.setSummary(isimage ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

        ipPreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                return true;
            }
        });
        imagePreference.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                //finish();
                return true;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        String serveur = (pref.getString("serveur", ""));
        ipPreference.setSummary(serveur);

        Boolean isimage = pref.getBoolean("image", false);
        imagePreference.setSummary(isimage ? getResources().getText(R.string.show) : getResources().getText(
                R.string.hide));

    }
}
