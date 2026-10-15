package com.stemroom.servlet;

import com.stemroom.dao.ProjectDao;
import com.stemroom.dao.SettingDao;
import com.stemroom.dao.UserDao;
import com.stemroom.model.Project;
import com.stemroom.model.ProjectStatus;
import com.stemroom.model.Role;
import com.stemroom.model.User;
import com.stemroom.util.Validation;
import com.stemroom.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Optional;

/**
 * Projects: the list, the new-project form and the detail page. The detail page
 * takes two kinds of POST, a status change and adding a member, told apart by "action".
 */
@WebServlet({"/projects", "/projects/new", "/projects/detail"})
public class ProjectServlet extends HttpServlet {

    private static final int MAX_TITLE = 120;
    private static final int MAX_DESCRIPTION = 1000;

    private final ProjectDao projectDao = new ProjectDao();
    private final UserDao userDao = new UserDao();
    private final SettingDao settingDao = new SettingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        switch (req.getServletPath()) {
            case "/projects" -> {
                req.setAttribute("projects", projectDao.findVisibleTo(user.getId()));
                WebUtil.show(req, resp, "projects/list");
            }
            case "/projects/new" -> WebUtil.show(req, resp, "projects/new");
            case "/projects/detail" -> showDetail(req, resp, user);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User user = WebUtil.currentUser(req);
        switch (req.getServletPath()) {
            case "/projects/new" -> createProject(req, resp, user);
            case "/projects/detail" -> updateProject(req, resp, user);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void createProject(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        String title = Validation.clean(req.getParameter("title"));
        String description = Validation.clean(req.getParameter("description"));
        String deadlineText = Validation.clean(req.getParameter("deadline"));
        LocalDate deadline = Validation.parseDate(deadlineText);
        int limit = Validation.parseIntOr(settingDao.get("max_projects_per_user", "10"), 10);

        String error = null;
        if (title.isEmpty() || title.length() > MAX_TITLE) {
            error = "Give the project a title of up to 120 characters.";
        } else if (description.length() > MAX_DESCRIPTION) {
            error = "The description is too long. Keep it under 1000 characters.";
        } else if (!deadlineText.isEmpty() && deadline == null) {
            error = "Use a real date for the deadline.";
        } else if (projectDao.countOwnedBy(user.getId()) >= limit) {
            error = "You have reached the project limit set by the admin.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("title", title);
            req.setAttribute("description", description);
            req.setAttribute("deadline", deadlineText);
            WebUtil.show(req, resp, "projects/new");
            return;
        }

        Project project = new Project(0, title, description.isEmpty() ? null : description,
                user.getId(), null, deadline, ProjectStatus.OPEN);
        int id = projectDao.insert(project);
        WebUtil.redirect(req, resp, "/projects/detail?id=" + id);
    }

    private void showDetail(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        int id = Validation.parseId(req.getParameter("id"));
        Optional<Project> project = id < 0 ? Optional.empty() : projectDao.findById(id);

        // A project the user cannot see gets the same answer as one that does not exist.
        if (project.isEmpty() || !projectDao.canAccess(id, user.getId())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        req.setAttribute("project", project.get());
        req.setAttribute("members", projectDao.findMembers(id));
        req.setAttribute("statuses", ProjectStatus.values());
        WebUtil.show(req, resp, "projects/detail");
    }

    private void updateProject(HttpServletRequest req, HttpServletResponse resp, User user)
            throws ServletException, IOException {
        int id = Validation.parseId(req.getParameter("id"));
        Optional<Project> project = id < 0 ? Optional.empty() : projectDao.findById(id);

        if (project.isEmpty() || !projectDao.canAccess(id, user.getId())) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String action = req.getParameter("action");
        if ("status".equals(action)) {
            // Anyone who can open the project may move it along.
            ProjectStatus.parse(req.getParameter("status"))
                    .ifPresent(status -> projectDao.updateStatus(id, status));
            WebUtil.redirect(req, resp, "/projects/detail?id=" + id);
        } else if ("member".equals(action)) {
            addMember(req, resp, user, project.get());
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /** Only the owner adds people, and only musician accounts can be added. */
    private void addMember(HttpServletRequest req, HttpServletResponse resp, User user, Project project)
            throws ServletException, IOException {
        Optional<User> invitee = userDao.findByEmail(Validation.normalizeEmail(req.getParameter("email")));

        String error = null;
        if (project.getOwnerId() != user.getId()) {
            error = "Only the owner can add people.";
        } else if (invitee.isEmpty() || invitee.get().getRole() != Role.MUSICIAN) {
            error = "No musician account uses that email.";
        } else if (invitee.get().getId() == project.getOwnerId()
                || projectDao.isMember(project.getId(), invitee.get().getId())) {
            error = "That musician is already on this project.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            showDetail(req, resp, user);
            return;
        }

        projectDao.addMember(project.getId(), invitee.get().getId());
        WebUtil.redirect(req, resp, "/projects/detail?id=" + project.getId());
    }
}
