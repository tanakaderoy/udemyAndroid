package com.tanaka.metaweather;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class WeatherHistory{
    private String title;

    private Integer woeid;
    private String locationType;

    public WeatherHistory(String title, Integer woeid, String locationType) {
        this.title = title;

        this.woeid = woeid;
        this.locationType = locationType;
    }

    public String getTitle() {
        return title;
    }


    public String getStringTimeStamp() {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("E, MMM dd h:mm a");
        String strDate = dateFormat.format(date);
        return strDate;
    }

    public Integer getWoeid() {
        return woeid;
    }

    public String getLocationType() {
        return locationType;
    }
}
