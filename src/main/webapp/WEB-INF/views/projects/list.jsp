<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Projects" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Projects</h1>
<p class="actions">
    <a class="button primary" href="${ctx}/projects/new">New project</a>
</p>

<c:choose>
    <c:when test="${empty projects}">
        <p class="empty">You are not on any projects yet.</p>
    </c:when>
    <c:otherwise>
        <div class="scroll">
            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Owner</th>
                    <th>Deadline</th>
                    <th>Status</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="p" items="${projects}">
                    <tr>
                        <td><a href="${ctx}/projects/detail?id=${p.id}"><c:out value="${p.title}"/></a></td>
                        <td><c:out value="${p.ownerName}"/></td>
                        <td class="meta">${p.deadlineText}</td>
                        <td><span class="status" data-status="${p.status}">${p.status.label}</span></td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
