package com.stemroom.filter;

import com.stemroom.dao.UserDao;
import com.stemroom.model.User;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Optional;

/**
 * Runs before every page that needs a login. It checks the session, loads the
 * current user from the database and blocks musicians from the admin pages.
 * The user is loaded on every request, so a role change applies at once.
 */
@WebFilter({"/dashboard", "/projects", "/projects/*", "/portfolio", "/admin/*"})
public class AuthFilter implements Filter {

    private final UserDao userDao = new UserDao();

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;

        HttpSession session = req.getSession(false);
        Object userId = session == null ? null : session.getAttribute("userId");
        Optional<User> user = userId == null ? Optional.empty() : userDao.findById((Integer) userId);

        if (user.isEmpty()) {
            if (session != null) {
                session.invalidate();
            }
            resp.sendRedirect(req.getContextPath() + "/login");
            return;
        }

        String path = req.getRequestURI().substring(req.getContextPath().length());
        if (path.startsWith("/admin") && !user.get().canModerate()) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        req.setAttribute("currentUser", user.get());
        chain.doFilter(request, response);
    }
}
