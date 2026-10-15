<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<c:set var="pageTitle" value="Moderation" scope="request"/>
<%@ include file="/WEB-INF/views/partials/header.jspf" %>

<h1>Moderation</h1>
<p class="lede">Songs are listed here as soon as they are uploaded. Pending songs come first.</p>

<c:choose>
    <c:when test="${empty songs}">
        <p class="empty">No songs have been uploaded yet.</p>
    </c:when>
    <c:otherwise>
        <div class="scroll">
            <table>
                <thead>
                <tr>
                    <th>Song</th>
                    <th>Owner</th>
                    <th>Status</th>
                    <th></th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="s" items="${songs}">
                    <tr>
                        <td>
                            <c:out value="${s.title}"/>
                            <div class="meta">
                                Album: <c:out value="${empty s.album ? 'none' : s.album}"/>.
                                File: <c:out value="${empty s.fileName ? 'none' : s.fileName}"/>.
                            </div>
                        </td>
                        <td><c:out value="${s.ownerName}"/></td>
                        <td><span class="status" data-status="${s.status}">${s.status.label}</span></td>
                        <td>
                            <div class="row-actions">
                                <form method="post" action="${ctx}/admin/moderation" class="inline">
                                    <input type="hidden" name="id" value="${s.id}">
                                    <button type="submit" name="decision" value="APPROVED" class="small">Approve</button>
                                </form>
                                <form method="post" action="${ctx}/admin/moderation" class="inline">
                                    <input type="hidden" name="id" value="${s.id}">
                                    <button type="submit" name="decision" value="REJECTED" class="small danger">Reject</button>
                                </form>
                            </div>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:otherwise>
</c:choose>

<%@ include file="/WEB-INF/views/partials/footer.jspf" %>
