package com.example.rushikesh.qpgadminaccount;

/**
 * Created by Rushikesh on 05/04/2018.
 */

public class PatternName {
    public String patternNameId;
    public String patternNameText;

    public PatternName(){

    }

    public PatternName(String patternNameId, String patternNameText) {
        this.patternNameId = patternNameId;
        this.patternNameText = patternNameText;
    }

    public String getPatternNameId() {
        return patternNameId;
    }

    public String getPatternNameText() {
        return patternNameText;
    }
}
