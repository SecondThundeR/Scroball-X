package su.secondthunder.scroball.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.collection.LongSparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.common.eventbus.Subscribe;
import su.secondthunder.scroball.R;
import su.secondthunder.scroball.ScroballApplication;
import su.secondthunder.scroball.Scrobble;
import su.secondthunder.scroball.db.ScroballDB;
import su.secondthunder.scroball.db.ScroballDBUpdateEvent;

import java.util.ArrayList;
import java.util.List;

public class ScrobbleHistoryFragment extends Fragment {

  private ArrayAdapter adapter;
  private ScroballDB scroballDB;
  private List<Scrobble> scrobbles = new ArrayList<>();
  private LongSparseArray<Scrobble> scrobbleMap = new LongSparseArray<>();

  @Nullable
  @Override
  public View onCreateView(
      LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
    View rootView = inflater.inflate(R.layout.fragment_scrobble_history, container, false);

    scroballDB = ((ScroballApplication) getActivity().getApplication()).getScroballDB();
    refreshData();

    adapter =
        new ScrobbleHistoryItemAdapter(
            getContext(), android.R.layout.simple_list_item_1, scrobbles);
    ListView listView = rootView.findViewById(R.id.scrobble_history_list_view);
    listView.setAdapter(adapter);
    return rootView;
  }

  @Override
  public void onResume() {
    super.onResume();
    ScroballApplication.getEventBus().register(this);
    refreshData();
  }

  @Override
  public void onPause() {
    super.onPause();
    ScroballApplication.getEventBus().unregister(this);
  }

  @Subscribe
  private void onScrobbleDBUpdate(ScroballDBUpdateEvent event) {
    final Scrobble scrobble = event.scrobble();
    final long id = scrobble.status().getDbId();

    getActivity()
        .runOnUiThread(
            () -> {
              if (scrobbleMap.get(id) != null) {
                scrobbleMap.get(id).status().setFrom(scrobble.status());
              } else {
                scrobbleMap.put(id, scrobble);
                adapter.insert(scrobble, 0);
              }

              adapter.notifyDataSetChanged();
            });
  }

  private void refreshData() {
    scrobbles.clear();
    scrobbles.addAll(scroballDB.readScrobbles());

    for (Scrobble scrobble : scrobbles) {
      scrobbleMap.put(scrobble.status().getDbId(), scrobble);
    }
  }
}
