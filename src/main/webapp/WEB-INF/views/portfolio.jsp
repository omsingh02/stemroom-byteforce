<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Portfolio" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Portfolio</h1>
<p class="lede">Each new song waits for an admin to approve it. The status column shows where it is.</p>

<h2>Add a song</h2>

<c:if test="${not empty error}">
    <p class="error"><c:out value="${error}"/></p>
</c:if>

<form method="post" action="${ctx}/portfolio" enctype="multipart/form-data" class="stack">
    <label>Title
        <input type="text" name="title" maxlength="120" required value="<c:out value='${title}'/>">
    </label>
    <label>Album
        <input type="text" name="album" maxlength="120" value="<c:out value='${album}'/>">
        <span class="hint">Optional.</span>
    </label>
    <label>Audio file
        <input type="file" name="audio" accept=".mp3,.wav,.flac">
        <span class="hint">mp3, wav or flac, up to 25 MB. Optional.</span>
    </label>
    <div class="actions">
        <button type="submit" class="primary">Add song</button>
    </div>
</form>

<h2>Your songs</h2>

<c:choose>
    <c:when test="${empty songs}">
        <p class="empty">No songs yet.</p>
    </c:when>
    <c:otherwise>
        <div class="scroll">
            <table>
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Album</th>
                    <th>File</th>
                    <th>Status</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${songs}">
                    <tr>
                        <td><c:out value="${s.title}"/></td>
                        <td><c:out value="${empty s.album ? 'none' : s.album}"/></td>
                        <td class="meta"><c:out value="${empty s.fileName ? 'no file' : s.fileName}"/></td>
                        <td><span class="status" data-status="${s.status}">${s.status.label}</span></td>
                        <td>
                            <form method="post" action="${ctx}/portfolio">
                                <input type="hidden" name="action" value="delete">
                                <input type="hidden" name="id" value="${s.id}">
                                <button type="submit" class="danger small">Delete</button>
                            </form>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
