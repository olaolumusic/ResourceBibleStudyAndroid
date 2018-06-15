package org.resourcecenterint.resourcebiblestudyandroid.model;

import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Ajose Olaolu on 20/10/2017.
 */

public class Chapters implements Comparable<Chapters>{
   private int ChapterId;
    private int BookId;
    private List<Verse> ChapterVerses;

    public int getChapterId() {
        return ChapterId;
    }

    public void setChapterId(int chapterId) {
        ChapterId = chapterId;
    }

    public int getBookId() {
        return BookId;
    }

    public void setBookId(int bookId) {
        BookId = bookId;
    }

    public List<Verse> getChapterVerses() {
        return ChapterVerses;
    }

    public void setChapterVerses(List<Verse> chapterVerses) {
        ChapterVerses = chapterVerses;
    }

    @Override
    public int compareTo(@NonNull Chapters chapters) {
        return this.getBookId();
    }
}
