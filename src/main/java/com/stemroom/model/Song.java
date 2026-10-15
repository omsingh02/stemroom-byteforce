package com.stemroom.model;

/** One song in a portfolio. The audio file is optional. */
public class Song {

    private final int id;
    private final int ownerId;
    private final String ownerName;
    private final String title;
    private final String album;
    private final String fileName;
    private final ModerationStatus status;

    public Song(int id, int ownerId, String ownerName, String title,
                String album, String fileName, ModerationStatus status) {
        this.id = id;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.title = title;
        this.album = album;
        this.fileName = fileName;
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public int getOwnerId() {
        return ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    /** The stored file name, or null when the song has no audio file. */
    public String getFileName() {
        return fileName;
    }

    public ModerationStatus getStatus() {
        return status;
    }
}
