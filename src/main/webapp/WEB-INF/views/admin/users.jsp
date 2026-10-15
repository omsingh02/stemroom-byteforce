<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Users" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Users</h1>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<h2>Add a user</h2>
<form method="post" action="${ctx}/admin/users" class="stack">
    <input type="hidden" name="action" value="create">
    <label>Name
        <input type="text" name="name" maxlength="80" required>
    </label>
    <label>Email
        <input type="email" name="email" required>
    </label>
    <label>Password
        <input type="password" name="password" minlength="6" required>
        <span class="hint">At least 6 characters.</span>
    </label>
    <label>Role
        <select name="role">
            <c:forEach var="r" items="${roles}">
                <option value="${r}">${r.label}</option>
            </c:forEach>
        </select>
    </label>
    <div class="actions">
        <button type="submit" class="primary">Add user</button>
    </div>
</form>

<h2>All accounts</h2>
<p class="hint">Deleting an account also deletes the projects and songs it owns.</p>

<div class="scroll">
    <table>
        <thead>
        <tr>
            <th>Name</th>
            <th>Email</th>
            <th>Role</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
            <tr>
                <td><c:out value="${u.name}"/></td>
                <td class="meta"><c:out value="${u.email}"/></td>
                <td>
                    <form method="post" action="${ctx}/admin/users" class="inline-form">
                        <input type="hidden" name="action" value="role">
                        <input type="hidden" name="id" value="${u.id}">
                        <select name="role" aria-label="Role">
                            <c:forEach var="r" items="${roles}">
                                <option value="${r}" ${r == u.role ? 'selected' : ''}>${r.label}</option>
                            </c:forEach>
                        </select>
                        <button type="submit" class="small">Save</button>
                    </form>
                </td>
                <td>
                    <c:if test="${u.id != currentUser.id}">
                        <form method="post" action="${ctx}/admin/users">
                            <input type="hidden" name="action" value="delete">
                            <input type="hidden" name="id" value="${u.id}">
                            <button type="submit" class="danger small">Delete</button>
                        </form>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
