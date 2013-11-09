<%@include file="/WEB-INF/jspf/taglib.jspf" %>
<%@include file="/WEB-INF/jspf/head.jspf"  %>

        <title>Lieberry - All books</title>
        
        <style type="text/css">
              .etal-popup {
                display: none;
                position: absolute;
                border: 1px solid #000;
                padding: 20px;
                padding-top: 5px;
                padding-bottom: 5px;
                background-color: #fff;
              }
              
              .etal-popup:hover {
                  display: block;
              }
              
              .etal-trigger{
                  padding-bottom: 5px;
              }
              
              .etal-trigger:hover + .etal-popup{
                  display: block;
              }
              
              img{
                  background-image: url("${pageContext.request.contextPath}/assets/book.png");
                  min-width: 40px;
                  min-height: 40px;
              }
              
        </style>

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
                
        <div>
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
                            <th>Authors</th>
                            <th>ISBN</th>
                            <th>Publisher</th>
                            <th>Publication year</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>
                    <c:forEach var="index" begin="0" end="${fn:length(books) - 1}">
                        <tr>
                            <td><a href="${pageContext.request.contextPath}/app/books/${books[index].isbn}" class="btn">View</a></td>
                            <td><img src="https://covers.openlibrary.org/b/isbn/<c:out value='${books[index].isbn}'/>-S.jpg" alt="Cover for <c:out value='${books[index].title}'/>." onerror="this.src='http://www.rip-factor.com/images/somethingawful.gif';"/></td>
                            <td><c:out value="${books[index].title}"/></td>
                            
                            <td>
                                <a href="${pageContext.request.contextPath}/app/books/author/${books[index].authors[0]}"><c:out value="${books[index].authors[0]}"/></a>
                                <c:if test="${fn:length(books[index].authors) > 1}">    
                                    <c:choose>
                                        <c:when test="${fn:length(books[index].authors) == 2}">
                                            <a href="${pageContext.request.contextPath}/app/books/author/${books[index].authors[1]}"><c:out value="${books[index].authors[1]}"/></a>
                                        </c:when>
                                        <c:otherwise>
                                            <br /><a class="etal-trigger" href="#">et al.</a>
                                            <div class="etal-popup">
                                                <c:forEach var="authorIndex" begin="1" end="${fn:length(books[index].authors) - 1}">
                                                    <a href="${pageContext.request.contextPath}/app/books/author/${books[index].authors[authorIndex]}"><c:out value="${books[index].authors[authorIndex]}"/></a><br/>
                                                </c:forEach>
                                            </div>
                                        </c:otherwise>
                                    </c:choose>
                                </c:if>
                            </td>

                            <td><c:out value="${books[index].isbn}"/></td>
                            <td><a href="${pageContext.request.contextPath}/app/books/publisher/${books[index].publisher}"><c:out value="${books[index].publisher}"/></a></td>
                            <td><a href="${pageContext.request.contextPath}/app/books/publicationyear/${books[index].publicationYear}"><c:out value="${books[index].publicationYear}"/></a></td>
                            <td>
                                <sec:authorize access="hasAnyRole('user', 'admin')">
                                    <a href="${pageContext.request.contextPath}/app/edit/${books[index].isbn}" class="btn">Edit</a>
                                </sec:authorize>
                            </td>
                            <td>
                                <sec:authorize access="hasAnyRole('user', 'admin')">
                                    <form:form action="${pageContext.request.contextPath}/app/books/${books[index].isbn}" method="DELETE"><input class="btn" type="submit" value="Delete"></form:form>
                                </sec:authorize>
                            </td>
                        </tr>
                    </c:forEach>
                    <thead>
                        <tr>
                            <th></th>
                            <th>Cover</th>
                            <th>Title</th>
                            <th>Authors</th>
                            <th>ISBN</th>
                            <th>Publisher</th>
                            <th>Publication year</th>
                            <th></th>
                            <th></th>
                        </tr>
                    </thead>
                </table>
                
                <div class="span12">
                    <div class="span1 offset3">
                        <c:if test="${pageNumber > 1}">
                            <a href="${requestScope['javax.servlet.forward.request_uri']}?pageNumber=${pageNumber - 1}" class="btn">Prev</a>
                        </c:if>
                    </div>
                    <div class="span1 offset1 text-center">
                        <span class="text-center">Page</span><br />
                        <span class="text-center">${pageNumber} of ${totalPages}</span>
                    </div>
                    <div class="span1 offset1">
                        <c:if test="${pageNumber < totalPages}">
                            <a href="${requestScope['javax.servlet.forward.request_uri']}?pageNumber=${pageNumber + 1}" class="btn">Next</a>
                        </c:if>
                    </div>
                </div>
            </c:if>    
        </div>

<%@include file="/WEB-INF/jspf/footer.jspf"  %>