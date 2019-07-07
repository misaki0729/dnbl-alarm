package io.github.misaki0729.dnbl.entity.db;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "subway")
public class Subway extends Model {

    public static final String SUBWAY_ID = "Id";
    public static final String AREA = "area";
    public static final String NAME = "name";
    public static final String COMPANY = "company";
    public static final String LASTUPDATE = "lastupdate";
    public static final String DELAY_TIME = "delay_time";
    public static final String INFO_URL = "info_url";

    @Column(name = AREA, notNull = true)
    public String area;

    @Column(name = NAME, notNull = true)
    public String name;

    @Column(name = COMPANY, notNull = true)
    public String company;

    @Column(name = DELAY_TIME)
    public int delay_time;

    @Column(name = LASTUPDATE)
    public long lastupdate;

    @Column(name = INFO_URL)
    public String info_url;

    public Subway() { super(); }

    public long getSubway_id() { return this.getId(); }
}
