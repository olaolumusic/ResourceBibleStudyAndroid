package org.resourcecenterint.resourcebiblestudyandroid.model;

import java.util.List;

/**
 * Created by Ajose Olaolu on 20/10/2017.
 */

public class Book  {
    private int Id;
    private String BookName;
    private List<Chapters> BookChapter;

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBookName() {
        return BookName;
    }

    public void setBookName(String bookName) {
        BookName = bookName;
    }

    public List<Chapters> getBookChapter() {
        return BookChapter;
    }

    public void setBookChapter(List<Chapters> bookChapter) {
        BookChapter = bookChapter;
    }

}
