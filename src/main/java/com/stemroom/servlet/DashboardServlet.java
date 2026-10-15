package com.stemroom.servlet;

import com.stemroom.dao.ProjectDao;
import com.stemroom.dao.SongDao;
import com.stemroom.dao.UserDao;
import com.stemroom.model.ModerationStatus;
import com.stemroom.model.User;
import com.stemroom.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** The home page. Admins and musicians get different content from the same page. */
@WebServlet("/dashboard")
public class DashboardServlet extends HttpServlet {

    private final ProjectDao projectDao = new ProjectDao();
    private final SongDao songDao = new SongDao();
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);

        if (user.canModerate()) {
            long pending = songDao.findAll().stream()
                    .filter(s -> s.getStatus() == ModerationStatus.PENDING)
                    .count();
            req.setAttribute("pendingCount", pending);
            req.setAttribute("userCount", userDao.findAll().size());
        } else {
            req.setAttribute("projects", projectDao.findVisibleTo(user.getId()));
            req.setAttribute("songs", songDao.findByOwner(user.getId()));
        }

        WebUtil.show(req, resp, "dashboard");
    }
}
