package com.example.android.booklisting;

/**
 * Created by rabum_alal on 11/17/17.
 */

public class Book {
    private String mTitle;
    private String mAuthor;

    public Book(String title, String author){
        mTitle = title;
        mAuthor = author;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
