package com.example.luhui1hao.newmp3player.model;

import java.io.Serializable;

/**
 * Created by luhui on 2016/6/21.
 */
public class Mp3Info implements Serializable{
    private long id; // 歌曲ID 3
    private String title; // 歌曲名称 0
    private String album; // 专辑 7
    private long albumId;//专辑ID 6
    private String displayName; //显示名称 4
    private String artist; // 歌手名称 2
    private long duration; // 歌曲时长 1
    private long size; // 歌曲大小 8
    private String url; // 歌曲路径 5
    private String lrcTitle; // 歌词名称
    private String lrcSize; // 歌词大小

    public Mp3Info(){
        super();
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public long getAlbumId() {
        return albumId;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getArtist() {
        return artist;
    }

    public long getDuration() {
        return duration;
    }

    public long getSize() {
        return size;
    }

    public String getUrl() {
        return url;
    }

    public String getLrcTitle() {
        return lrcTitle;
    }

    public String getLrcSize() {
        return lrcSize;
    }

    public void setId(long id) {

        this.id = id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setAlbumId(long albumId) {
        this.albumId = albumId;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setLrcTitle(String lrcTitle) {
        this.lrcTitle = lrcTitle;
    }

    public void setLrcSize(String lrcSize) {
        this.lrcSize = lrcSize;
    }
}
