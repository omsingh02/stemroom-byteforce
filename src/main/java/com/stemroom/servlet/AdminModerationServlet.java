package com.stemroom.servlet;

import com.stemroom.dao.SongDao;
import com.stemroom.model.ModerationStatus;
import com.stemroom.util.Validation;
import com.stemroom.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/** Admin page for songs: approve or reject each upload. */
@WebServlet("/admin/moderation")
public class AdminModerationServlet extends HttpServlet {

    private final SongDao songDao = new SongDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("songs", songDao.findAll());
        WebUtil.show(req, resp, "admin/moderation");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        int id = Validation.parseId(req.getParameter("id"));
        Optional<ModerationStatus> decision = ModerationStatus.parse(req.getParameter("decision"));

        // Only approve or reject. Putting a song back to pending is not offered.
        if (id < 0 || decision.isEmpty() || decision.get() == ModerationStatus.PENDING) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        songDao.updateStatus(id, decision.get());
        WebUtil.redirect(req, resp, "/admin/moderation");
    }
}
