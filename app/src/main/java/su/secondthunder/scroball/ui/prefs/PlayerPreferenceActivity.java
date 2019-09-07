package su.secondthunder.scroball.ui.prefs;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import su.secondthunder.scroball.R;

public class PlayerPreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme_Prefs);
        setContentView(R.layout.blank);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment, new PlayerPreferencesFragment())
                .commit();
    }
}
