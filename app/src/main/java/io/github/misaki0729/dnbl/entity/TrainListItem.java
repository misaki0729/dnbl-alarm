package io.github.misaki0729.dnbl.entity;

import java.util.ArrayList;
import java.util.List;

import io.github.misaki0729.dnbl.entity.db.Subway;
import io.github.misaki0729.dnbl.util.Preferences;

public class TrainListItem {
    private long subwayId;
    private String company;
    private String name;
    private boolean isChecked;

    public TrainListItem(long subwayId, String company, String name, boolean isChecked) {
        this.subwayId = subwayId;
        this.company = company;
        this.name = name;
        this.isChecked = isChecked;
    }

    public long getSubwayId() {
        return subwayId;
    }

    public void setSubwayId(long subwayId) {
        this.subwayId = subwayId;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public static List<TrainListItem> createTrainListItemList(List<Subway> trains) {
        List<TrainListItem> list = new ArrayList<>();
        long currentSubwayId = new Preferences().getSubwayId();
        boolean isChecked = false;

        for (Subway item: trains) {
            if (currentSubwayId == item.getSubway_id()) isChecked = true;
            else isChecked = false;

            TrainListItem trainListItem = new TrainListItem(item.getSubway_id(), item.company, item.name, isChecked);
            list.add(trainListItem);
        }

        return list;
    }
}
