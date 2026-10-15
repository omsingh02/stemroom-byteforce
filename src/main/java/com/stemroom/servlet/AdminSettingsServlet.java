package com.stemroom.servlet;

import com.stemroom.dao.SettingDao;
import com.stemroom.util.Validation;
import com.stemroom.util.WebUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/** Admin page for the two settings: sign-ups on or off, and projects per musician. */
@WebServlet("/admin/settings")
public class AdminSettingsServlet extends HttpServlet {

    private final SettingDao settingDao = new SettingDao();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        showPage(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // An unticked box sends nothing at all, so "present" means "ticked".
        boolean allowRegistration = req.getParameter("allow_registration") != null;
        int maxProjects = Validation.parseIntOr(req.getParameter("max_projects_per_user"), -1);

        if (maxProjects < 1 || maxProjects > 100) {
            req.setAttribute("error", "Projects per musician must be a whole number from 1 to 100.");
            showPage(req, resp);
            return;
        }

        settingDao.save("allow_registration", String.valueOf(allowRegistration));
        settingDao.save("max_projects_per_user", String.valueOf(maxProjects));
        WebUtil.redirect(req, resp, "/admin/settings?saved=1");
    }

    private void showPage(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        req.setAttribute("allowRegistration", "true".equals(settingDao.get("allow_registration", "true")));
        req.setAttribute("maxProjects", settingDao.get("max_projects_per_user", "10"));
        WebUtil.show(req, resp, "admin/settings");
    }
}
