package com.stemroom.servlet;

import com.stemroom.dao.UserDao;
import com.stemroom.model.Role;
import com.stemroom.model.User;
import com.stemroom.util.PasswordUtil;
import com.stemroom.util.Validation;
import com.stemroom.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

/** Admin page for accounts: add one, change its role, or delete it. */
@WebServlet("/admin/users")
public class AdminUserServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        showList(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        User me = WebUtil.currentUser(req);
        String action = req.getParameter("action");

        if ("create".equals(action)) {
            create(req, resp);
        } else if ("role".equals(action)) {
            changeRole(req, resp, me);
        } else if ("delete".equals(action)) {
            delete(req, resp, me);
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    private void create(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = Validation.clean(req.getParameter("name"));
        String email = Validation.normalizeEmail(req.getParameter("email"));
        String password = req.getParameter("password");
        Optional<Role> role = Role.parse(req.getParameter("role"));

        String error = null;
        if (name.isEmpty() || name.length() > 80) {
            error = "Name must be 1 to 80 characters.";
        } else if (!Validation.isEmail(email)) {
            error = "That email does not look right.";
        } else if (password == null || password.length() < 6) {
            error = "Password needs at least 6 characters.";
        } else if (role.isEmpty()) {
            error = "Pick a role.";
        } else if (userDao.findByEmail(email).isPresent()) {
            error = "An account with that email already exists.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            showList(req, resp);
            return;
        }

        userDao.insert(User.create(role.get(), 0, name, email, PasswordUtil.hash(password)));
        WebUtil.redirect(req, resp, "/admin/users");
    }

    private void changeRole(HttpServletRequest req, HttpServletResponse resp, User me)
            throws ServletException, IOException {
        int id = Validation.parseId(req.getParameter("id"));
        Optional<Role> role = Role.parse(req.getParameter("role"));

        if (id < 0 || role.isEmpty()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (id == me.getId()) {
            req.setAttribute("error", "You cannot change your own role.");
            showList(req, resp);
            return;
        }

        userDao.updateRole(id, role.get());
        WebUtil.redirect(req, resp, "/admin/users");
    }

    private void delete(HttpServletRequest req, HttpServletResponse resp, User me)
            throws ServletException, IOException {
        int id = Validation.parseId(req.getParameter("id"));

        if (id < 0) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (id == me.getId()) {
            req.setAttribute("error", "You cannot delete the account you are using.");
            showList(req, resp);
            return;
        }

        userDao.deleteById(id);
        WebUtil.redirect(req, resp, "/admin/users");
    }

    private void showList(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("users", userDao.findAll());
        req.setAttribute("roles", Role.values());
        WebUtil.show(req, resp, "admin/users");
    }
}
