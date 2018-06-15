package org.resourcecenterint.resourcebiblestudyandroid.model;

import java.util.ArrayList;

/**
 * Created by Ajose Olaolu on 20/10/2017.
 */

public class Bible
{
    private String Name;
    private String ShortName;
    private String Version;
    private ArrayList<Book> Books;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getShortName() {
        return ShortName;
    }

    public void setShortName(String shortName) {
        ShortName = shortName;
    }

    public String getVersion() {
        return Version;
    }

    public void setVersion(String version) {
        Version = version;
    }

    public ArrayList<Book> getBooks() {
        return Books;
    }

    public void setBooks(ArrayList<Book> books) {
        Books = books;
    }

}