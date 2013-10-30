<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - Import search results</title>

<%@include file="/WEB-INF/jspf/header.jspf"  %>

        <h1>Import a book</h1>
        <p class="lead">Import book(s) from Open Library.</p>

        <div>
            <form action="${pageContext.request.contextPath}/app/import" method="POST" class="form-horizontal">
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
                    </div>
                </div>

                <div class="controls-row">
                    <br />
                    <input value="Search" type="submit" class="btn btn-large btn-primary"/>
                </div>
            </form>
        </div>

        <div>
            <c:choose>
                <c:when test="${not empty books}">
                    <table class="table">
                        <thead>
                            <tr>
                                <th>Cover</th>
                                <th>Title</th>
                                <th>Authors</th>
                                <th>ISBN</th>
                                <th>Publisher</th>
                                <th>Publication year</th>
                                <th></th>
                            </tr>
                        </thead>
                        <c:forEach var="index" begin="0" end="${fn:length(books) - 1}">
                            <tr>
                                <td><img src="https://covers.openlibrary.org/b/isbn/<c:out value='${books[index].isbn}'/>-S.jpg" alt="Cover for <c:out value='${books[index].title}'/>." /></td>
                                <td><c:out value="${books[index].title}"/></td>

                                <td>
                                    <c:forEach var="author" items="${books[index].authors}">
                                        <c:out value="${author}" />
                                    </c:forEach>
                                </td>

                                <td><c:out value="${books[index].isbn}"/></td>
                                <td><c:out value="${books[index].publisher}"/></td>
                                <td><c:out value="${books[index].publicationYear}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="$inLibrary[index]">
                                            <span style="color: green; font-weight: bold;">In library</span>
                                        </c:when>
                                        <c:otherwise>
                                            <a href="#">Add</a>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        <thead>
                            <tr>
                                <th>Cover</th>
                                <th>Title</th>
                                <th>Authors</th>
                                <th>ISBN</th>
                                <th>Publisher</th>
                                <th>Publication year</th>
                                <th></th>
                            </tr>
                        </thead>
                    </table>
                    <div class="span12">
                        <div class="span1 offset3">
                            <c:if test="${pageNumber > 1}">
                                <a href="${pageContext.request.contextPath}/app/import?pageNumber=${pageNumber - 1}" class="btn">Prev</a>
                            </c:if>
                        </div>
                        <div class="span1 offset1 text-center">
                            <span class="text-center">Page</span><br />
                            <span class="text-center">${pageNumber} of ${totalPages}</span>
                        </div>
                        <div class="span1 offset1">
                            <c:if test="${pageNumber < totalPages}">
                                <a href="${pageContext.request.contextPath}/app/import?pageNumber=${pageNumber + 1}" class="btn">Next</a>
                            </c:if>
                        </div>
                    </div>
                </c:when>
                <c:otherwise>
                    <p>No matching results.</p>
                </c:otherwise>
            </c:choose>
        </div>

<%@include file="/WEB-INF/jspf/footer.jspf"  %>