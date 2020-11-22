package org.resourcecenterint.resourcebiblestudyandroid.model;

/**
 * Created by Ajose Olaolu on 20/10/2017.
 */
public class DailyScriptures {
    private int Id;
    private String FirstReading;
    private String BookVersion;
    private int DayOfTheYear;
    private String ThirdReading;
    private String SecondReading;

    public int getId() {
        return this.Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public String getFirstReading() {
        return this.FirstReading;
    }

    public void setFirstReading(String FirstReading) {
        this.FirstReading = FirstReading;
    }

    public String getSecondReading() {
        return this.SecondReading;
    }

    public void setSecondReading(String SecondReading) {
        this.SecondReading = SecondReading;
    }

    public String getThirdReading() {
        return this.ThirdReading;
    }

    public void setThirdReading(String ThirdReading) {
        this.ThirdReading = ThirdReading;
    }

    public String getBookVersion() {
        return this.BookVersion;
    }

    public void setBookVersion(String BookVersion) {
        this.BookVersion = BookVersion;
    }

    public int getDayOfTheYear() {
        return this.DayOfTheYear;
    }

    public void setDayOfTheYear(int DayOfTheYear) {
        this.DayOfTheYear = DayOfTheYear;
    }
}
