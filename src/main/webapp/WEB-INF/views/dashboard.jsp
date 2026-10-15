<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Home" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Home</h1>

<c:choose>
    <c:when test="${currentUser.canModerate()}">
        <dl class="facts">
            <dt>Songs waiting for review</dt>
            <dd>${pendingCount}</dd>
            <dt>Accounts</dt>
            <dd>${userCount}</dd>
        </dl>
        <div class="actions">
            <a class="button primary" href="${ctx}/admin/moderation">Review songs</a>
            <a class="button" href="${ctx}/admin/users">Manage users</a>
            <a class="button" href="${ctx}/admin/settings">Settings</a>
        </div>
    </c:when>
    <c:otherwise>
        <h2>Projects</h2>
        <c:choose>
            <c:when test="${empty projects}">
                <p class="empty">No projects yet. <a href="${ctx}/projects/new">Start one</a>.</p>
            </c:when>
            <c:otherwise>
                <ul class="plain">
                    <c:forEach var="p" items="${projects}">
                        <li>
                            <a href="${ctx}/projects/detail?id=${p.id}"><c:out value="${p.title}"/></a>
                            <span class="status" data-status="${p.status}">${p.status.label}</span>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>

        <h2>Songs</h2>
        <c:choose>
            <c:when test="${empty songs}">
                <p class="empty">No songs yet. <a href="${ctx}/portfolio">Add one</a>.</p>
            </c:when>
            <c:otherwise>
                <ul class="plain">
                    <c:forEach var="s" items="${songs}">
                        <li>
                            <c:out value="${s.title}"/>
                            <span class="status" data-status="${s.status}">${s.status.label}</span>
                        </li>
                    </c:forEach>
                </ul>
            </c:otherwise>
        </c:choose>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
