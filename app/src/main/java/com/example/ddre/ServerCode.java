package com.example.ddre;

public enum ServerCode {
    NEW_EMPTY_DECAL(1000),
    NEW_TEXT_DECAL(1001),
    NEW_IMAGE_DECAL(1002),
    NEW_NOTES_DECAL(1003),
    NEW_PAPER_DECAL(1004),
    NEW_AUTHOR_DECAL(1005),
    NEW_REFS_DECAL(1006);

    private int mValue;

    ServerCode(int value) {
        mValue = value;
    }

    public int value()
    {
        return mValue;
    }}
