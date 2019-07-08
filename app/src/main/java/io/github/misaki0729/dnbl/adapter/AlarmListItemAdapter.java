package io.github.misaki0729.dnbl.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import io.github.misaki0729.dnbl.R;
import io.github.misaki0729.dnbl.entity.AlarmListItem;

public class AlarmListItemAdapter extends ArrayAdapter<AlarmListItem> {
    private LayoutInflater inflater;
    AlarmListItem data;

    class ViewHolder {
        ImageView isEnabled;
        TextView hour;
        TextView minute;
        TextView description;
    }

    public AlarmListItemAdapter(Context context, List<AlarmListItem> list) {
        super(context, 0, list);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.alarm_list_item, parent, false);
            holder = new ViewHolder();
            holder.description = (TextView) convertView.findViewById(R.id.alarm_description);
            holder.hour = (TextView) convertView.findViewById(R.id.alarm_hour);
            holder.minute = (TextView) convertView.findViewById(R.id.alarm_minute);
            holder.isEnabled = (ImageView) convertView.findViewById(R.id.alarm_is_enabled);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        data = getItem(position);
        holder.hour.setText(String.format("%02d", data.getHour()));
        holder.minute.setText(String.format("%02d", data.getMinute()));
        holder.description.setText(data.getDescription());

        return convertView;
    }
}
