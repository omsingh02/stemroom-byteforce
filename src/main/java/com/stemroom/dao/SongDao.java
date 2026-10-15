package com.stemroom.dao;

import com.stemroom.model.ModerationStatus;
import com.stemroom.model.Song;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

/** Reads and writes the songs table. */
public class SongDao implements CrudDao<Song> {

    private static final String SELECT =
            "SELECT s.id, s.owner_id, u.name AS owner_name, s.title, s.album, s.file_name, s.status "
            + "FROM songs s JOIN users u ON u.id = s.owner_id";

    @Override
    public List<Song> findAll() {
        // Pending songs come first, so the moderation page opens on the work still to do.
        return Db.query(SELECT + " ORDER BY (s.status = 'PENDING') DESC, s.id DESC", SongDao::mapRow);
    }

    public List<Song> findByOwner(int ownerId) {
        return Db.query(SELECT + " WHERE s.owner_id = ? ORDER BY s.id DESC", SongDao::mapRow, ownerId);
    }

    @Override
    public Optional<Song> findById(int id) {
        return Db.first(Db.query(SELECT + " WHERE s.id = ?", SongDao::mapRow, id));
    }

    @Override
    public int insert(Song song) {
        return Db.insert("INSERT INTO songs (owner_id, title, album, file_name, status) "
                        + "VALUES (?, ?, ?, ?, ?)",
                song.getOwnerId(), song.getTitle(), song.getAlbum(), song.getFileName(),
                song.getStatus().name());
    }

    public void updateStatus(int id, ModerationStatus status) {
        Db.update("UPDATE songs SET status = ? WHERE id = ?", status.name(), id);
    }

    @Override
    public void deleteById(int id) {
        Db.update("DELETE FROM songs WHERE id = ?", id);
    }

    private static Song mapRow(ResultSet rs) throws SQLException {
        return new Song(
                rs.getInt("id"),
                rs.getInt("owner_id"),
                rs.getString("owner_name"),
                rs.getString("title"),
                rs.getString("album"),
                rs.getString("file_name"),
                ModerationStatus.valueOf(rs.getString("status")));
    }
}
