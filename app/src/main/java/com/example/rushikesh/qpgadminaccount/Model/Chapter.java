package com.example.rushikesh.qpgadminaccount.Model;

/**
 * Created by Rushikesh on 05/04/2018.
 */

public class Chapter {
    public String chapterId;
    public String chapterName;

    public Chapter(){

    }

    public Chapter(String chapterId, String chapterName) {
        this.chapterId = chapterId;
        this.chapterName = chapterName;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }
}
