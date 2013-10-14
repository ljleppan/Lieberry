<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - Import a book</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron ">
            <h1>Import a book</h1>
            <p class="lead">Import a book from Open Library by entering its ISBN number.</p>
        </div>

        <div>
            <form action="${pageContext.request.contextPath}/app/import" method="POST" class="form-horizontal">
                <div class="control-group">
                    <label for="isbn" class="control-label">
                        ISBN
                    </label>
                    <div class="controls">
                        <input type="text" name="isbn" />
                        <span class="help-inline">Accepted forms are f.ex. "10-2023-123123-X" and "9780980200447"</span>
                    </div>
                </div>
                
                <div class="form-actions">
                    <input value="Import" type="submit" class="btn btn-large btn-primary"/>
                </div>
            </form>
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf"  %>
