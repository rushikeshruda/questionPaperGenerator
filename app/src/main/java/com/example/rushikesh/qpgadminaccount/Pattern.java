package com.example.rushikesh.qpgadminaccount;

/**
 * Created by Rushikesh on 04/04/2018.
 */

public class Pattern {

    public String patternText;
    public String patternId;

    public Pattern(){

    }

    public Pattern(String patternId, String patternText) {
        this.patternText = patternText;
        this.patternId = patternId;
    }

    public String getPatternText() {
        return patternText;
    }

    public String getPatternId() {
        return patternId;
    }
}
