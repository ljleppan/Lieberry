<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>
    <div class="hero-unit text-center">
        <h1>Lieberry</h1>
        <p class="lead">Lorem ipsum, lieberry is fun</p>
    </div>
    <div class="control-group text-center">
        <a href="books" class="btn btn-large input-block-level">Browse library</a>
    </div>
    <sec:authorize access="hasAnyRole('user', 'admin')">
    <div class="control-group text-center">
        <a href="add" class="btn btn-large input-block-level">Add a book</a>
    </div>
    <div class="control-group text-center">
        <a href="import" class="btn btn-large input-block-level">Import a book</a>
    </div>
    </sec:authorize>
    
    <div class="control-group text-center">
        <sec:authorize access="!hasAnyRole('user', 'admin')">
            <a href="register" class="btn btn-large input-block-level">Register</a>
        </sec:authorize>
    </div>
    
    <div class="control-group text-center">
        <sec:authorize access="hasRole('admin')">
            <a href="users" class="btn btn-large input-block-level btn-info">User Management</a>
        </sec:authorize>
    </div>
    
                
<%@include file="/WEB-INF/jspf/footer.jspf"  %>
