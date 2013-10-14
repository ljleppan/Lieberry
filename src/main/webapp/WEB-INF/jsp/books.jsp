<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - All books</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <div class="hero-unit text-center">
            <h1>Browse books</h1>
            
            <form action="${pageContext.request.contextPath}/app/search" method="POST" class="form-horizontal">
                <div class="controls-group">
                    <div class="controls-row">
                        <input type="text" name="query" class="search-query input-xlarge" />
                    </div>

                    <div class="controls-row">                   
                        <label for="title" class="radio inline">Title</label>
                        <input type="radio" name="field" id="title" value="title" checked="true"/> 

                        <label for="author" class="radio inline">Author</label>
                        <input type="radio" name="field" id="author" value="author" />

                        <label for="isbn" class="radio inline">ISBN</label>
                        <input type="radio" name="field" id="isbn" value="isbn" />

                        <label for="publisher" class="radio inline">Publisher</label>
                        <input type="radio" name="field" id="publisher" value="publisher" />

                        <label for="publicationYear" class="radio inline">Publication year</label>
                        <input type="radio" name="field" id="publicationYear" value="publicationYear" />
                    </div>
                </div>
                
                <div class="controls-row">
                    <br />
                    <input value="Search" type="submit" class="btn btn-large btn-primary"/>
                </div>
            </form>
        </div>
                
        <div class="span12">
            <p>
                Displaying page ${pageNumber} of ${totalPages} for ${totalItems} books matching the query.
            </p>
        </div>

        <div>
            <c:if test="${not empty books}">
                <table class="table">
                    <thead>
                        <tr>
                            <th></th>
                            <th>Cover</th>
                            <th>Title</th>
                            <th>Author</th>
                            <th>ISBN</th>
                            <th>Publisher</th>
                            <th>Publication year</th>
                            <th></th>
                        </tr>
                    </thead>
                    <c:forEach var="book" items="${books}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/app/books/${book.isbn}" class="btn">View</a></td>
                            <td><img src="https://covers.openlibrary.org/b/isbn/<c:out value='${book.isbn}'/>-S.jpg" alt="Cover for <c:out value='${book.title}'/>." /></td>
                            <td><c:out value="${book.title}"/></td>
                            <td>
                                <c:forEach var="author" items="${book.authors}">
                                    <a href="${pageContext.request.contextPath}/app/books/author/${author}"><c:out value="${author}"/><br /></a>
                                </c:forEach>
                            </td>
                            <td><c:out value="${book.isbn}"/></td>
                            <td><a href="${pageContext.request.contextPath}/app/books/publisher/${book.publisher}"><c:out value="${book.publisher}"/></a></td>
                            <td><a href="${pageContext.request.contextPath}/app/books/publicationyear/${book.publicationYear}"><c:out value="${book.publicationYear}"/></a></td>
                            <td><form:form action="${pageContext.request.contextPath}/app/books/${book.isbn}" method="DELETE"><input class="btn" type="submit" value="Delete"></form:form>
                        </tr>
                    </c:forEach>
                </table>
                
                <div class="span12">
                    <div class="span1 offset3">
                        <c:if test="${pageNumber > 1}">
                            <a href="${pageContext.request.contextPath}/app/books?pageNumber=${pageNumber - 1}" class="btn">Prev</a>
                        </c:if>
                    </div>
                    <div class="span1 offset1 text-center">
                        <span class="text-center">Page</span><br />
                        <span class="text-center">${pageNumber} of ${totalPages}</span>
                    </div>
                    <div class="span1 offset1">
                        <c:if test="${pageNumber < totalPages}">
                            <a href="${pageContext.request.contextPath}/app/books?pageNumber=${pageNumber + 1}" class="btn">Next</a>
                        </c:if>
                    </div>
                </div>
            </c:if>    
        </div>

<%@include file="/WEB-INF/jspf/footer.jspf"  %>