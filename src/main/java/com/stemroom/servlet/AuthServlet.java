package com.stemroom.servlet;

import com.stemroom.dao.SettingDao;
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
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

/** Log in, make an account and log out. One servlet, so the session code lives in one place. */
@WebServlet({"/login", "/register", "/logout"})
public class AuthServlet extends HttpServlet {

    private final UserDao userDao = new UserDao();
    private final SettingDao settingDao = new SettingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (req.getServletPath()) {
            case "/login" -> WebUtil.show(req, resp, "auth/login");
            case "/register" -> {
                req.setAttribute("closed", !registrationOpen());
                WebUtil.show(req, resp, "auth/register");
            }
            case "/logout" -> {
                HttpSession session = req.getSession(false);
                if (session != null) {
                    session.invalidate();
                }
                WebUtil.redirect(req, resp, "/login");
            }
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        switch (req.getServletPath()) {
            case "/login" -> login(req, resp);
            case "/register" -> register(req, resp);
            default -> resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private void login(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String email = Validation.normalizeEmail(req.getParameter("email"));
        String password = req.getParameter("password");

        Optional<User> found = userDao.findByEmail(email);
        boolean valid = found.isPresent() && password != null
                && PasswordUtil.matches(password, found.get().getPasswordHash());

        if (!valid) {
            // Same message for an unknown email and a wrong password, so the form
            // does not reveal which emails have accounts.
            req.setAttribute("error", "Email or password is wrong.");
            req.setAttribute("email", email);
            WebUtil.show(req, resp, "auth/login");
            return;
        }

        startSession(req, found.get().getId());
        WebUtil.redirect(req, resp, "/dashboard");
    }

    private void register(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = Validation.clean(req.getParameter("name"));
        String email = Validation.normalizeEmail(req.getParameter("email"));
        String password = req.getParameter("password");
        boolean open = registrationOpen();

        String error = null;
        if (!open) {
            error = "New sign-ups are closed for now.";
        } else if (name.isEmpty() || name.length() > 80) {
            error = "Name must be 1 to 80 characters.";
        } else if (!Validation.isEmail(email)) {
            error = "That email does not look right.";
        } else if (password == null || password.length() < 6) {
            error = "Password needs at least 6 characters.";
        } else if (userDao.findByEmail(email).isPresent()) {
            error = "An account with that email already exists.";
        }

        if (error != null) {
            req.setAttribute("error", error);
            req.setAttribute("name", name);
            req.setAttribute("email", email);
            req.setAttribute("closed", !open);
            WebUtil.show(req, resp, "auth/register");
            return;
        }

        int id = userDao.insert(User.create(Role.MUSICIAN, 0, name, email, PasswordUtil.hash(password)));
        startSession(req, id);
        WebUtil.redirect(req, resp, "/dashboard");
    }

    /** A fresh session on every login, so an old session id cannot be reused. */
    private void startSession(HttpServletRequest req, int userId) {
        HttpSession old = req.getSession(false);
        if (old != null) {
            old.invalidate();
        }
        req.getSession(true).setAttribute("userId", userId);
    }

    private boolean registrationOpen() {
        return "true".equals(settingDao.get("allow_registration", "true"));
    }
}
