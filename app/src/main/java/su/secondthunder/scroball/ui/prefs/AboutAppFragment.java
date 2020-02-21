package su.secondthunder.scroball.ui.prefs;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import su.secondthunder.scroball.R;

public class AboutAppFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_about, rootKey);
    }
}
