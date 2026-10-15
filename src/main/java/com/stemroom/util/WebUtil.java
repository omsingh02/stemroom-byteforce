package com.stemroom.util;

import com.stemroom.model.User;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Helpers the servlets share: showing a page, redirecting and reading the logged-in user. */
public final class WebUtil {

    private WebUtil() {
    }

    /** Forwards to a page under WEB-INF/views. The browser never sees the JSP path. */
    public static void show(HttpServletRequest req, HttpServletResponse resp, String page)
            throws ServletException, IOException {
        RequestDispatcher view = req.getRequestDispatcher("/WEB-INF/views/" + page + ".jsp");
        view.forward(req, resp);
    }

    /** Redirects to a path in this web app, for example "/projects". */
    public static void redirect(HttpServletRequest req, HttpServletResponse resp, String path)
            throws IOException {
        resp.sendRedirect(req.getContextPath() + path);
    }

    /** The user that AuthFilter loaded for this request. Only call this behind the filter. */
    public static User currentUser(HttpServletRequest req) {
        return (User) req.getAttribute("currentUser");
    }
}
