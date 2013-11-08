<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - User Management</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron ">
            <h1>User Management</h1>
        </div>
        
        <div>
            <c:if test="${not empty users}">
                <table class="table table-striped">
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Roles</th>
                            <th>Tools</th>
                            <th></th>
                        </tr>
                    </thead>
                    <c:forEach var="index" begin="0" end="${fn:length(users) - 1}">
                        <tr>
                            <td><c:out value="${users[index].username}"/></td>
                            <td>
                                <c:forEach var="role" items="${users[index].roles}">
                                    <c:out value="${role.rolename}" /><br />
                                </c:forEach>
                            </td>
                            <td>
                                <form:form action="${pageContext.request.contextPath}/app/users/${users[index].id}/admin" method="POST"><input class="btn" type="submit" value="Toggle Admin"></form:form>
                            </td>
                            <td>
                                <form:form action="${pageContext.request.contextPath}/app/users/${users[index].id}" method="DELETE"><input class="btn" type="submit" value="Delete"></form:form>
                            </td>
                        </tr>
                    </c:forEach>
                    <thead>
                        <tr>
                            <th>Username</th>
                            <th>Roles</th>
                            <th>Tools</th>
                            <th></th>
                        </tr>
                    </thead>
                </table>
            </c:if>
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf"  %>
