package com.stemroom.dao;

import com.stemroom.model.Project;
import com.stemroom.model.ProjectStatus;
import com.stemroom.model.User;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/** Reads and writes projects and the people in them. */
public class ProjectDao implements CrudDao<Project> {

    private static final String SELECT =
            "SELECT p.id, p.title, p.description, p.owner_id, u.name AS owner_name, p.deadline, p.status "
            + "FROM projects p JOIN users u ON u.id = p.owner_id";

    @Override
    public List<Project> findAll() {
        return Db.query(SELECT + " ORDER BY p.id DESC", ProjectDao::mapRow);
    }

    /** Projects the user owns or was added to. This feeds the Projects page. */
    public List<Project> findVisibleTo(int userId) {
        return Db.query(SELECT + " WHERE p.owner_id = ? OR p.id IN "
                        + "(SELECT project_id FROM project_members WHERE user_id = ?) ORDER BY p.id DESC",
                ProjectDao::mapRow, userId, userId);
    }

    @Override
    public Optional<Project> findById(int id) {
        return Db.first(Db.query(SELECT + " WHERE p.id = ?", ProjectDao::mapRow, id));
    }

    @Override
    public int insert(Project project) {
        return Db.insert("INSERT INTO projects (title, description, owner_id, deadline, status) "
                        + "VALUES (?, ?, ?, ?, ?)",
                project.getTitle(), project.getDescription(), project.getOwnerId(),
                project.getDeadline(), project.getStatus().name());
    }

    public void updateStatus(int id, ProjectStatus status) {
        Db.update("UPDATE projects SET status = ? WHERE id = ?", status.name(), id);
    }

    @Override
    public void deleteById(int id) {
        Db.update("DELETE FROM projects WHERE id = ?", id);
    }

    public int countOwnedBy(int userId) {
        return Db.query("SELECT COUNT(*) FROM projects WHERE owner_id = ?",
                rs -> rs.getInt(1), userId).get(0);
    }

    /** True when the user owns the project or was added to it. */
    public boolean canAccess(int projectId, int userId) {
        int matches = Db.query("SELECT COUNT(*) FROM projects p WHERE p.id = ? AND (p.owner_id = ? "
                        + "OR p.id IN (SELECT project_id FROM project_members WHERE user_id = ?))",
                rs -> rs.getInt(1), projectId, userId, userId).get(0);
        return matches > 0;
    }

    public boolean isMember(int projectId, int userId) {
        return !Db.query("SELECT user_id FROM project_members WHERE project_id = ? AND user_id = ?",
                rs -> rs.getInt(1), projectId, userId).isEmpty();
    }

    public void addMember(int projectId, int userId) {
        Db.update("INSERT INTO project_members (project_id, user_id) VALUES (?, ?)", projectId, userId);
    }

    /** The added musicians. The owner is not in this list. */
    public List<User> findMembers(int projectId) {
        return Db.query("SELECT u.id, u.name, u.email, u.password_hash, u.role "
                        + "FROM project_members m JOIN users u ON u.id = m.user_id "
                        + "WHERE m.project_id = ? ORDER BY u.name",
                UserDao::mapRow, projectId);
    }

    static Project mapRow(ResultSet rs) throws SQLException {
        return new Project(
                rs.getInt("id"),
                rs.getString("title"),
                rs.getString("description"),
                rs.getInt("owner_id"),
                rs.getString("owner_name"),
                rs.getObject("deadline", LocalDate.class),
                ProjectStatus.valueOf(rs.getString("status")));
    }
}
