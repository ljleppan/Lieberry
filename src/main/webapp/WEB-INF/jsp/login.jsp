<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - <c:out value="${book.title}" /></title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron">
            <h1>Login</h1>
            <c:if test="${not empty error}">
		<div class="alert alert-error">
			Your login attempt was not successful, try again.<br /> 
                        Error: ${sessionScope["SPRING_SECURITY_LAST_EXCEPTION"].message}
		</div>
            </c:if>
        </div>
        <div>
            <form class="form-horizontal" name='f' action="${pageContext.request.contextPath}/app/perform_login" method='POST'>
                <div class="control-group">
                    <label for="j_username" class="control-label">
                        Username
                    </label>
                    <div class="controls">
                        <input type="text" id="j_username" name="j_username" placeholder="Username">
                    </div>
                </div>
                <div class="control-group">
                    <label for="j_password" class="control-label">
                        Password
                    </label>
                    <div class="controls">
                        <input type="password" id="j_password" name="j_password" placeholder="Password" />
                    </div>
                </div>
                <div class="control-group">
                    <div class="controls">
                        <input class="btn btn-primary" name="submit" type="submit" value="submit" />
                    </div>
                </div>
            </form>
        </div>
    
<%@include file="/WEB-INF/jspf/footer.jspf"  %>
