package com.stemroom.servlet;

import com.stemroom.dao.SongDao;
import com.stemroom.model.ModerationStatus;
import com.stemroom.model.Song;
import com.stemroom.model.User;
import com.stemroom.util.Validation;
import com.stemroom.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;

/**
 * A musician's songs. Audio files are saved outside the web app folder,
 * so a redeploy does not wipe them.
 */
@WebServlet("/portfolio")
@MultipartConfig(maxFileSize = 25 * 1024 * 1024)
public class PortfolioServlet extends HttpServlet {

    private static final Set<String> AUDIO_TYPES = Set.of("mp3", "wav", "flac");
    private static final Path UPLOAD_DIR = Paths.get(System.getProperty("user.home"), "stemroom-uploads");

    private final SongDao songDao = new SongDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        showList(req, resp, WebUtil.currentUser(req));
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        if ("delete".equals(req.getParameter("action"))) {
            deleteSong(req, resp, user);
        } else {
            addSong(req, resp, user);
        }
    }

    private void addSong(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String title = Validation.clean(req.getParameter("title"));
        String album = Validation.clean(req.getParameter("album"));
        Part audio = req.getPart("audio");
        boolean hasFile = audio != null && audio.getSize() > 0;
        String original = hasFile ? audio.getSubmittedFileName() : null;

        String error = null;
        if (title.isEmpty() || title.length() > 120) {
            error = "Give the song a title of up to 120 characters.";
        } else if (album.length() > 120) {
            error = "The album name is too long.";
        } else if (hasFile && !AUDIO_TYPES.contains(extension(original))) {
            error = "Only mp3, wav or flac files can be uploaded.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("title", title);
            req.setAttribute("album", album);
            showList(req, resp, user);
            return;
        }

        String storedName = null;
        if (hasFile) {
            // The timestamp keeps two uploads with the same name from overwriting each other.
            storedName = System.currentTimeMillis() + "_" + safeName(original);
            Files.createDirectories(UPLOAD_DIR);
            audio.write(UPLOAD_DIR.resolve(storedName).toString());
        }

        songDao.insert(new Song(0, user.getId(), null, title,
                album.isEmpty() ? null : album, storedName, ModerationStatus.PENDING));
        WebUtil.redirect(req, resp, "/portfolio");
    }

    private void deleteSong(HttpServletRequest req, HttpServletResponse resp, User user)
            throws IOException {
        int id = Validation.parseId(req.getParameter("id"));
        Optional<Song> song = id < 0 ? Optional.empty() : songDao.findById(id);

        // Only the owner can delete. Anyone else is sent back to the list without a change.
        if (song.isPresent() && song.get().getOwnerId() == user.getId()) {
            songDao.deleteById(id);
            if (song.get().getFileName() != null) {
                Files.deleteIfExists(UPLOAD_DIR.resolve(song.get().getFileName()));
            }
        }
        WebUtil.redirect(req, resp, "/portfolio");
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        req.setAttribute("songs", songDao.findByOwner(user.getId()));
        WebUtil.show(req, resp, "portfolio");
    }

    private static String extension(String fileName) {
        int dot = fileName.lastIndexOf('.');
        return dot < 0 ? "" : fileName.substring(dot + 1).toLowerCase(Locale.ROOT);
    }

    /** Keeps letters, digits, dots, dashes and underscores. Everything else becomes an underscore. */
    private static String safeName(String fileName) {
        return fileName.replaceAll("[^A-Za-z0-9._-]", "_");
    }
}
