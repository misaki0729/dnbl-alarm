package io.github.misaki0729.dnbl.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubwayDelayInfo {

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Info {
        public String name;
        public String company;
        public long lastupdate_gmt;
    }
}
