package com.example.rushikesh.qpgadminaccount;

/**
 * Created by Rushikesh on 04/04/2018.
 */

public class Level {

    public String levelName;
    public String levelId;

    public Level(){

    }

    public Level(String levelId, String levelName) {
        this.levelName = levelName;
        this.levelId = levelId;
    }

    public String getLevelName() {
        return levelName;
    }

    public String getLevelId() {
        return levelId;
    }
}
