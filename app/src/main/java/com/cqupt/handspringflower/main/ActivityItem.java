package com.cqupt.handspringflower.main;

public class ActivityItem {
    private int mImageId;
    private String mTitle;
    private String mTime;
    private String mInstitute;
    private String mLocation;
    private String mContent;
    private int mAutorId;
    private String mAuthor;

    public ActivityItem(int imageId, String title,
                        String time, String institute,
                        String location, String content,
                        int autorId, String author) {
        mImageId = imageId;
        mTitle = title;
        mTime = time;
        mInstitute = institute;
        mLocation = location;
        mContent = content;
        mAutorId = autorId;
        mAuthor = author;
    }

    public int getImageId() {
        return mImageId;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public String getInstitute() {
        return mInstitute;
    }

    public String getLocation() {
        return mLocation;
    }

    public String getContent() {
        return mContent;
    }

    public int getAutorId() {
        return mAutorId;
    }

    public String getAuthor() {
        return mAuthor;
    }
}
