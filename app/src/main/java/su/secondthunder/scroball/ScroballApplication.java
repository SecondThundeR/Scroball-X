package su.secondthunder.scroball;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;

import su.secondthunder.scroball.db.ScroballDB;
import com.raizlabs.android.dbflow.config.FlowManager;

import java.util.Locale;

import com.softartdev.lastfm.Caller;

public class ScroballApplication extends Application {

  private static EventBus eventBus = new EventBus();
  private static NowPlayingChangeEvent lastEvent =
      NowPlayingChangeEvent.builder().source("").track(Track.empty()).build();

  private LastfmClient lastfmClient;
  private ScroballDB scroballDB;
  private SharedPreferences sharedPreferences;

  @Override
  public void onCreate() {
    super.onCreate();
    FlowManager.init(this);

    sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

    String userAgent =
        String.format(Locale.UK, "%s.%d", BuildConfig.APPLICATION_ID, BuildConfig.VERSION_CODE);
    String sessionKeyKey = getString(R.string.saved_session_key);
    LastfmApi api = new LastfmApi();
    Caller caller = Caller.getInstance();

    if (sharedPreferences.contains(sessionKeyKey)) {
      String sessionKey = sharedPreferences.getString(sessionKeyKey, null);
      lastfmClient = new LastfmClient(api, caller, userAgent, sessionKey);
    } else {
      lastfmClient = new LastfmClient(api, caller, userAgent);
    }

    scroballDB = new ScroballDB();
    eventBus.register(this);
  }

  public void startListenerService() {
    if (ListenerService.isNotificationAccessEnabled(this) && getLastfmClient().isAuthenticated()) {
      startService(new Intent(this, ListenerService.class));
    }
  }

  public void stopListenerService() {
    stopService(new Intent(this, ListenerService.class));
  }

  public void logout() {
    SharedPreferences preferences = getSharedPreferences();
    SharedPreferences.Editor editor = preferences.edit();
    editor.remove(getString(R.string.saved_session_key));
    editor.apply();

    stopListenerService();
    getScroballDB().clear();
    getLastfmClient().clearSession();
  }

  public LastfmClient getLastfmClient() {
    return lastfmClient;
  }

  public ScroballDB getScroballDB() {
    return scroballDB;
  }

  public SharedPreferences getSharedPreferences() {
    return sharedPreferences;
  }

  @Subscribe
  public void onNowPlayingChange(NowPlayingChangeEvent event) {
    lastEvent = event;
  }

  @Subscribe
  public void onAuthError(AuthErrorEvent event) {
    logout();
  }

  public static EventBus getEventBus() {
    return eventBus;
  }

  public static NowPlayingChangeEvent getLastNowPlayingChangeEvent() {
    return lastEvent;
  }
}
