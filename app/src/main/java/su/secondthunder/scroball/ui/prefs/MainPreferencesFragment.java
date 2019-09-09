package su.secondthunder.scroball.ui.prefs;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.preference.ListPreference;
import androidx.preference.PreferenceFragmentCompat;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import su.secondthunder.scroball.R;
import su.secondthunder.scroball.db.ScroballDB;

public class MainPreferencesFragment extends PreferenceFragmentCompat {

    private ScroballDB scroballDB;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.pref_main, rootKey);
        scroballDB = new ScroballDB();
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
        findPreference("clear_adapter").setOnPreferenceClickListener(preference -> {
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(R.string.pref_header_clear_listview)
                    .setMessage(R.string.clear_listview_question)
                    .setPositiveButton(
                            android.R.string.yes,
                            (dialog, whichButton) -> {
                                getScroballDB().clear();
                                Intent restartIntent = getContext().getPackageManager()
                                        .getLaunchIntentForPackage(getContext().getPackageName());
                                restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(restartIntent);
                            })
                    .setNegativeButton(android.R.string.no, null)
                    .show();
            return false;
        });
        findPreference("about_app").setOnPreferenceClickListener(preference -> {
            View view = getLayoutInflater().inflate(R.layout.about_app_dialog, null);
            TextView mod_devs = view.findViewById(R.id.mod_devs);
            mod_devs.setText(R.string.mod_devs);
            mod_devs.setMovementMethod(LinkMovementMethod.getInstance());
            new MaterialAlertDialogBuilder(getContext())
                    .setTitle(R.string.pref_header_about_app)
                    .setView(view)
                    .setNegativeButton(R.string.alert_close, null)
                    .show();
            return false;
        });
    }

    public ScroballDB getScroballDB() {
        return scroballDB;
    }
}
