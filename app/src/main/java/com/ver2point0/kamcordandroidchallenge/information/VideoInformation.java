package com.ver2point0.kamcordandroidchallenge.information;

public class VideoInformation {

    private String mTitle;
    private String mThumbnail;
    private String mVideoUrl;

    public VideoInformation(String title, String thumbnail, String videoUrl) {
        mTitle = title;
        mThumbnail = thumbnail;
        mVideoUrl = videoUrl;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getThumbnail() {
        return mThumbnail;
    }

    public void setThumbnail(String thumbnail) {
        mThumbnail = thumbnail;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        mVideoUrl = videoUrl;
    }
}
