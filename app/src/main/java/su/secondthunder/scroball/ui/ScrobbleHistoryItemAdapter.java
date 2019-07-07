package su.secondthunder.scroball.ui;

import android.content.Context;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import su.secondthunder.scroball.R;
import su.secondthunder.scroball.Scrobble;

import java.text.DateFormat;
import java.util.List;

public class ScrobbleHistoryItemAdapter extends ArrayAdapter<Scrobble> {

  public ScrobbleHistoryItemAdapter(
      @NonNull Context context, @LayoutRes int resource, @NonNull List<Scrobble> objects) {
    super(context, resource, objects);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    Scrobble scrobble = getItem(position);

    ViewHolder viewHolder;
    if (convertView == null) {
      viewHolder = new ViewHolder();
      LayoutInflater inflater = LayoutInflater.from(getContext());
      convertView = inflater.inflate(R.layout.scrobble_history_item, parent, false);

      viewHolder.title = convertView.findViewById(R.id.scrobble_history_item_title);
      viewHolder.artist = convertView.findViewById(R.id.scrobble_history_item_artist);
      viewHolder.timestamp = convertView.findViewById(R.id.scrobble_history_item_timestamp);

      viewHolder.successIcon = convertView.findViewById(R.id.scrobble_history_item_success_icon);
      viewHolder.pendingIcon = convertView.findViewById(R.id.scrobble_history_item_pending_icon);
      viewHolder.warningIcon = convertView.findViewById(R.id.scrobble_history_item_warning_icon);

      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }

    viewHolder.title.setText(scrobble.track().track());
    viewHolder.artist.setText(scrobble.track().artist());
    viewHolder.timestamp.setText(
        DateUtils.formatSameDayTime(
            scrobble.timestamp() * 1000L,
            System.currentTimeMillis(),
            DateFormat.SHORT,
            DateFormat.SHORT));

    viewHolder.successIcon.setVisibility(View.GONE);
    viewHolder.pendingIcon.setVisibility(View.GONE);
    viewHolder.warningIcon.setVisibility(View.GONE);

    switch (scrobble.status().getErrorCode()) {
      case -1:
        viewHolder.successIcon.setVisibility(View.VISIBLE);
        break;

      case 0:
        viewHolder.pendingIcon.setVisibility(View.VISIBLE);
        break;

      default:
        viewHolder.warningIcon.setVisibility(View.VISIBLE);
    }

    return convertView;
  }

  private static class ViewHolder {
    TextView title;
    TextView artist;
    TextView timestamp;
    ImageView successIcon;
    ImageView warningIcon;
    ImageView pendingIcon;
  }
}
