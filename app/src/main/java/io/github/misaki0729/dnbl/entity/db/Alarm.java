package io.github.misaki0729.dnbl.entity.db;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Alarm")
public class Alarm extends Model {

    public static final String ALARM_ID = "Id";
    public static final String TIME = "time";
    public static final String IS_ENABLE = "is_enable";
    public static final String DOW = "dow";
    public static final String SUBWAY = "subway";
    public static final String DESCRIPTION = "description";
    public static final String ALARM_MUSIC_ID_NORMAL = "alarm_music_id_normal";
    public static final String ALARM_MUSIC_ID_DELAY = "alarm_music_id_delay";

    @Column(name = TIME, notNull = true)
    public long time;

    @Column(name = IS_ENABLE, notNull = true)
    public boolean is_enable;

    @Column(name = DOW)
    public String dow;

    @Column(name = SUBWAY)
    public Subway subway;

    @Column(name = DESCRIPTION)
    public String description;

    @Column(name = ALARM_MUSIC_ID_NORMAL, notNull = true)
    public int alarm_music_id_normal;

    @Column(name = ALARM_MUSIC_ID_DELAY)
    public int alarm_music_id_delay;

    public Alarm() { super(); }

    public long getAlarm_id() { return this.getId(); }
}
