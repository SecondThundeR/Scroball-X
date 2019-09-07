package su.secondthunder.scroball.ui.prefs;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import su.secondthunder.scroball.R;

public class MainPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);
        findPreference("theme").setSummaryProvider(ListPreference.SimpleSummaryProvider.getInstance());
        findPreference("theme").setOnPreferenceChangeListener((preference, newValue) -> {
            switch (Integer.parseInt((String) newValue)) {
                case 0:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case 1:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
                case 2:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    break;
            }
            return true;
        });
        findPreference("notifications").setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), NotificationPreferenceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return false;
        });
        findPreference("players").setOnPreferenceClickListener(preference -> {
            Intent intent = new Intent(getActivity(), PlayerPreferenceActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            return false;
        });
    }
}
