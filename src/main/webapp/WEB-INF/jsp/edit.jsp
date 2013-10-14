<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - Edit a book</title>

        <script>
            var rowNum = ${fn:length(book.authors)} -1;
            function addRow() {
                rowNum++;
                var row = ' <div id="author'+rowNum+'Grp" class="control-group">\n\
                                <div id="author'+rowNum+'Div" class="controls">\n\
                                    <input id="author'+rowNum+'" name="authors['+rowNum+']" type="text" value=""/>\n\
                                    \n\
                                </div>\n\
                            </div>';
                jQuery('#authors').append(row);
            }
            
            function removeRow() {
                if (rowNum > 0){
                    jQuery('#author'+rowNum+'Grp').remove();
                    rowNum--;
                }
            }
            
        </script>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="jumbotron ">
            <h1>Edit a book</h1>
            <p class="lead">All fields must be filled.</p>
        </div>

        <div>
            <form:form modelAttribute="book" action="${pageContext.request.contextPath}/app/books/${book.isbn}" method="POST" class="form-horizontal">
                <div class="control-group">
                    <label for="title" class="control-label">
                        Title
                    </label>
                    <div class="controls">
                        <form:input id="title" path="title"/>
                        <form:errors path="title" cssClass="help-inline" cssStyle="color: red;"/>
                    </div>
                </div>
                
                <div id="authors">
                    <div id ="author0Grp" class="control-group">
                        <label for="author" class="control-label">
                            Authors
                        </label>
                        <div id="author0Div" class="controls">
                            <form:input id="author0" path="authors[0]" />
                            <input type="button" class="btn" value="Add" onClick="addRow();" />
                            <input type="button" class="btn" value="Remove" onClick="removeRow();" />
                            <form:errors path="authors[0]" cssClass="help-inline" cssStyle="color: red;"/>
                        </div>
                    </div>
                    <c:if test="${fn:length(book.authors) > 1}">
                        <c:forEach var="index" begin="1" end="${fn:length(book.authors) - 1}">
                            <div id="author${index}Grp" class="control-group">
                                <div id="author${index}Div" class="controls">
                                    <form:input id="author${index}" path="authors[${index}]" />
                                </div>
                            </div>
                        </c:forEach>
                    </c:if>
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