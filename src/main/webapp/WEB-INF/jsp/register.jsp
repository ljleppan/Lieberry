<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - Register</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron ">
            <h1>Register</h1>
        </div>

        <c:if test="${not empty usernameError}">
            <div class="alert alert-error">
                <c:out value="${usernameError}" /><br /> 
            </div>
        </c:if>
        <c:if test="${not empty passwordError}">
            <div class="alert alert-error">
                    <c:out value="${passwordError}" /><br /> 
            </div>
        </c:if>
        
        <div>
            <form class="form-horizontal" action="${pageContext.request.contextPath}/app/register" method='POST'>
                <div class="control-group">
                    <label for="username" class="control-label">
                        Username
                    </label>
                    <div class="controls">
                        <input type="text" id="username" name="username" placeholder="Username">
                    </div>
                </div>
                <div class="control-group">
                    <label for="password" class="control-label">
                        Password
                    </label>
                    <div class="controls">
                        <input type="password" id="password" name="password" placeholder="Password" />
                    </div>
                </div>
                <div class="control-group">
                    <label for="password2" class="control-label">
                        Repeat password
                    </label>
                    <div class="controls">
                        <input type="password" id="password" name="password2" placeholder="Password" />
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
