<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - Add a book</title>
        
        <script>
            $(function() {
                var addDiv = $('#authorsDiv');
                var i = $('#addinput p').size() + 1;

                $('#addNew').live('click', function() {
                    $('<p><form:input id="authors" path="authors" /><a href="#" id="remNew" class="btn">Remove</a></p>').appendTo(addDiv);
                    i++;

                    return false;
                });

                $('#remNew').live('click', function() {
                    if( i > 2 ) {
                    $(this).parents('p').remove();
                    i--;
                    }
                    return false;
                });
            });

        </script>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron ">
            <h1>Add a new book</h1>
            <p class="lead">All fields must be filled.</p>
        </div>

        <div>
            <form:form modelAttribute="book" action="${pageContext.request.contextPath}/app/books" method="POST" class="form-horizontal">
                <div class="control-group">
                    <label for="title" class="control-label">
                        Title
                    </label>
                    <div class="controls">
                        <form:input id="title" path="title"/>
                        <form:errors path="title" cssClass="help-inline" cssStyle="color: red;"/>
                    </div>
                </div>
                
                <div class="control-group">
                    <label for="author" class="control-label">
                        Author
                    </label>
                    <div id="authorsDiv" class="controls">
                        <p>
                            <form:input id="authors" path="authors" />
                            <a href="#" id="addNew" class="btn">Add</a>
                            <form:errors path="authors" cssClass="help-inline" cssStyle="color: red;"/>
                        <p>
                    </div>
                </div>
                
                <div class="control-group">
                    <label for="isbn" class="control-label">
                        ISBN
                    </label>
                    <div class="controls">
                        <form:input id="isbn" path="isbn" />
                        <span class="help-inline">Numbers, dashes and possibly a final X.</span>
                        <form:errors path="isbn" cssClass="help-inline" cssStyle="color: red;"/>
                    </div>
                </div>
                
                <div class="control-group">
                    <label for="publisher" class="control-label">
                        Publisher
                    </label>
                    <div class="controls">
                        <form:input id="publisher" path="publisher" />
                        <form:errors path="publisher" cssClass="help-inline" cssStyle="color: red;"/>
                    </div>
                </div>
                
                <div class="control-group">
                    <label for="publicationYear" class="control-label">
                        Publication year
                    </label>
                    <div class="controls">
                        <form:input id="publicationYear" path="publicationYear" />
                        <span class="help-inline">Number larger than zero.</span>
                        <form:errors path="publicationYear" cssClass="help-inline" cssStyle="color: red;"/>
                    </div>
                </div>
                
                <div class="form-actions">
                    <input value="Save" type="submit" class="btn btn-large btn-primary"/>
                </div>
            </form:form>
        </div>
<%@include file="/WEB-INF/jspf/footer.jspf"  %>