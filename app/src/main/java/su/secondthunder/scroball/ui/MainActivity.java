package su.secondthunder.scroball.ui;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.preference.PreferenceManager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.google.common.collect.ImmutableList;
import su.secondthunder.scroball.R;
import su.secondthunder.scroball.ScroballApplication;
import su.secondthunder.scroball.ui.prefs.ModernPreferenceActivity;

import java.util.List;

public class MainActivity extends AppCompatActivity{

  public static final String EXTRA_INITIAL_TAB = "initial_tab";
  public static final int TAB_NOW_PLAYING = 0;
  public static final int TAB_SCROBBLE_HISTORY = 1;
  private ScroballApplication application;

  private SectionsPagerAdapter mSectionsPagerAdapter;
  private ViewPager mViewPager;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    switch (Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(this).getString("theme", "0"))) {
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
    setContentView(R.layout.activity_main);

    application = (ScroballApplication) getApplication();
    application.startListenerService();

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);
    // Create the adapter that will return a fragment for each of the three
    // primary sections of the activity.
    mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

    // Set up the ViewPager with the sections adapter.
    mViewPager = findViewById(R.id.container);
    mViewPager.setAdapter(mSectionsPagerAdapter);

    TabLayout tabLayout = findViewById(R.id.tabs);
    tabLayout.setupWithViewPager(mViewPager);

    // Initial tab may have been specified in the intent.
    int initialTab = getIntent().getIntExtra(EXTRA_INITIAL_TAB, TAB_NOW_PLAYING);
    mViewPager.setCurrentItem(initialTab);

  }

  @Override
  protected void onResume() {
    super.onResume();
  }

  @Override
  protected void onPause() {
    super.onPause();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.menu_main, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.settings_item:
        Intent intent = new Intent(getBaseContext(), ModernPreferenceActivity.class);
        startActivityForResult(intent, 1);
        return true;
      case R.id.logout_item:
        logout();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  public void logout() {
    new MaterialAlertDialogBuilder(this)
        .setTitle(R.string.are_you_sure)
        .setMessage(R.string.logout_confirm)
        .setPositiveButton(
            android.R.string.yes,
            (dialog, whichButton) -> {
              application.logout();

              Intent intent = new Intent(this, SplashScreen.class);
              intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
              startActivity(intent);
              finish();
            })
        .setNegativeButton(android.R.string.no, null)
        .show();
  }

  /**
   * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the
   * sections/tabs/pages.
   */
  public class SectionsPagerAdapter extends FragmentPagerAdapter {

    List<Fragment> fragments =
        ImmutableList.of(new NowPlayingFragment(), new ScrobbleHistoryFragment());

    public SectionsPagerAdapter(FragmentManager fm) {
      super(fm);
    }

    @Override
    public Fragment getItem(int position) {
      // getItem is called to instantiate the fragment for the given page.
      // Return a PlaceholderFragment (defined as a static inner class below).
      return fragments.get(position);
    }

    @Override
    public int getCount() {
      return fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return getString(R.string.tab_now_playing);
        case 1:
          return getString(R.string.tab_history);
      }
      return null;
    }
  }
}
