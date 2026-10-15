<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="${project.title}" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1><c:out value="${project.title}"/></h1>

<dl class="details">
    <dt>Owner</dt>
    <dd><c:out value="${project.ownerName}"/></dd>
    <dt>Deadline</dt>
    <dd>${project.deadlineText}</dd>
    <dt>Status</dt>
    <dd><span class="status" data-status="${project.status}">${project.status.label}</span></dd>
</dl>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<c:if test="${not empty project.description}">
    <p class="prose"><c:out value="${project.description}"/></p>
</c:if>

<h2>Status</h2>
<form method="post" action="${ctx}/projects/detail" class="inline-form">
    <input type="hidden" name="id" value="${project.id}">
    <input type="hidden" name="action" value="status">
    <select name="status" aria-label="Project status">
        <c:forEach var="s" items="${statuses}">
            <option value="${s}" ${s == project.status ? 'selected' : ''}>${s.label}</option>
        </c:forEach>
    </select>
    <button type="submit">Update status</button>
</form>

<h2>People</h2>
<ul class="plain">
    <li>
        <c:out value="${project.ownerName}"/>
        <span class="meta">owner</span>
    </li>
    <c:forEach var="m" items="${members}">
        <li>
            <c:out value="${m.name}"/>
            <span class="meta"><c:out value="${m.email}"/></span>
        </li>
    </c:forEach>
</ul>

<c:if test="${project.ownerId == currentUser.id}">
    <h2>Add a musician</h2>
    <form method="post" action="${ctx}/projects/detail" class="inline-form">
        <input type="hidden" name="id" value="${project.id}">
        <input type="hidden" name="action" value="member">
        <input type="email" name="email" placeholder="musician@example.com" required aria-label="Musician email">
        <button type="submit">Add</button>
    </form>
    <p class="hint">Only musician accounts can be added. Use the email the musician signed up with.</p>
</c:if>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
