package su.secondthunder.scroball.ui.prefs;

import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;
import androidx.preference.SwitchPreference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import su.secondthunder.scroball.R;

public class PlayerPreferencesFragment extends PreferenceFragmentCompat {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_notification, rootKey);

        SharedPreferences sharedPreferences = this.getPreferenceManager().getSharedPreferences();
        Set<String> prefKeys = sharedPreferences.getAll().keySet();
        List<String> playerPrefKeys = new ArrayList<>();

        for (String key : prefKeys) {
            if (key.startsWith("player.")) {
                playerPrefKeys.add(key);
            }
        }

        PreferenceScreen root = getPreferenceScreen();
        PackageManager packageManager = root.getContext().getApplicationContext().getPackageManager();

        for (String key : playerPrefKeys) {
            Preference preference = new SwitchPreference(root.getContext());
            String packageName = key.substring(7);
            ApplicationInfo applicationInfo;

            try {
                applicationInfo = packageManager.getApplicationInfo(packageName, 0);
            } catch (PackageManager.NameNotFoundException e) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.remove(key);
                editor.apply();

                continue;
            }

            preference.setTitle(packageManager.getApplicationLabel(applicationInfo));
            preference.setKey(key);
            preference.setIcon(packageManager.getApplicationIcon(applicationInfo));
            root.addPreference(preference);
        }
    }
}
