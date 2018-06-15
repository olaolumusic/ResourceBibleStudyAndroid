package org.resourcecenterint.resourcebiblestudyandroid.model;

/**
 * Created by Ajose Olaolu on 20/10/2017.
 */public class DailyScriptures
{
    private int Id;

    public int getId() { return this.Id; }

    public void setId(int Id) { this.Id = Id; }

    private String FirstReading;

    public String getFirstReading() { return this.FirstReading; }

    public void setFirstReading(String FirstReading) { this.FirstReading = FirstReading; }

    private String SecondReading;

    public String getSecondReading() { return this.SecondReading; }

    public void setSecondReading(String SecondReading) { this.SecondReading = SecondReading; }

    private String ThirdReading;

    public String getThirdReading() { return this.ThirdReading; }

    public void setThirdReading(String ThirdReading) { this.ThirdReading = ThirdReading; }

    private String BookVersion;

    public String getBookVersion() { return this.BookVersion; }

    public void setBookVersion(String BookVersion) { this.BookVersion = BookVersion; }

    private int DayOfTheYear;

    public int getDayOfTheYear() { return this.DayOfTheYear; }

    public void setDayOfTheYear(int DayOfTheYear) { this.DayOfTheYear = DayOfTheYear; }
}
