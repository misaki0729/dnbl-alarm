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
import io.github.misaki0729.dnbl.entity.TrainListItem;

public class TrainListItemAdapter extends ArrayAdapter<TrainListItem> {
    private LayoutInflater inflater;
    TrainListItem data;

    class ViewHolder {
        TextView company;
        TextView name;
        ImageView isCheked;
    }

    public TrainListItemAdapter(Context context, List<TrainListItem> list) {
        super(context, 0, list);
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.subway_list_item, parent, false);
            holder = new ViewHolder();
            holder.company = (TextView) convertView.findViewById(R.id.subway_company_textview);
            holder.name = (TextView) convertView.findViewById(R.id.subway_name_textview);
            holder.isCheked = (ImageView) convertView.findViewById(R.id.train_select_imageview);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        data = getItem(position);
        holder.company.setText(data.getCompany());
        holder.name.setText(data.getName());
        if (data.isChecked()) holder.isCheked.setVisibility(View.VISIBLE);
        else holder.isCheked.setVisibility(View.INVISIBLE);

        return convertView;
    }
}
