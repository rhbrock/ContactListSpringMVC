<%-- 
    Document   : contacts
    Created on : Jul 23, 2018, 9:55:45 AM
    Author     : Roger Brock
--%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Company Contacts</title>
        <!-- Bootstrap core CSS -->
        <link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet">        
    </head>
    <body>
        <div class="container">
            <h1>Company Contacts</h1>
            <hr/>
            <div class="navbar">
                <ul class="nav nav-tabs">
                    <li role="presentation" ><a href="${pageContext.request.contextPath}/index.jsp">Home</a></li>
                    <li role="presentation" class="active"><a href="${pageContext.request.contextPath}/displayContactsPage">Contacts</a></li>
                    <li role="presentation"><a href="${pageContext.request.contextPath}/displaySearchPage">Search</a></li>
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <li role="presentation">
                            <a href="${pageContext.request.contextPath}/displayUserList">
                                User Admin
                            </a>
                        </li>                        
                    </sec:authorize>

                </ul>    
            </div>

            <c:if test="${pageContext.request.userPrincipal.name != null}">
                <p>Hello : ${pageContext.request.userPrincipal.name}
                    | <a href="<c:url value="/j_spring_security_logout" />" > Logout</a>
                </p>
            </c:if>

            <!-- 
                Add a row to our container - this will hold the summary table and the new
                contact form.
            -->
            <div class="row">
                <!-- 
                    Add a col to hold the summary table - have it take up half the row 
                -->
                <div class="col-md-6">
                    <h2>My Contacts</h2>
                    <table id="contactTable" class="table table-hover">
                        <tr>
                            <th width="40%">Contact Name</th>
                            <th width="30%">Company</th>
                            <th width="15%"></th>
                            <th width="15%"></th>
                        </tr>
                        <c:forEach var="currentContact" items="${contactList}">
                            <tr>
                                <td>
                                    <a href="displayContactDetails?contactId=${currentContact.contactId}">
                                        <c:out value="${currentContact.firstName}"/> <c:out value="${currentContact.lastName}"/>
                                    </a>
                                </td>
                                <td>
                                    <c:out value="${currentContact.company}"/>
                                </td>
                                <td>
                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        <a href="displayEditContactForm?contactId=${currentContact.contactId}">
                                            Edit
                                        </a>
                                    </sec:authorize>
                                </td>
                                <td>
                                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                                        <a href="deleteContact?contactId=${currentContact.contactId}">
                                            Delete
                                        </a>
                                    </sec:authorize>
                                </td>
                            </tr>
                        </c:forEach>
                    </table>                    
                </div> <!-- End col div -->
                <!-- 
                    Add col to hold the new contact form - have it take up the other 
                    half of the row
                -->
                <div class="col-md-6">
                    <sec:authorize access="hasRole('ROLE_ADMIN')">
                        <h2>Add New Contact</h2>
                        <form class="form-horizontal" 
                              role="form" method="POST" 
                              action="createContact">
                            <div class="form-group">
                                <label for="add-first-name" class="col-md-4 control-label">First Name:</label>
                                <div class="col-md-8">
                                    <input type="text" class="form-control" name="firstName" placeholder="First Name"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="add-last-name" class="col-md-4 control-label">Last Name:</label>
                                <div class="col-md-8">
                                    <input type="text" class="form-control" name="lastName" placeholder="Last Name"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="add-company" class="col-md-4 control-label">Company:</label>
                                <div class="col-md-8">
                                    <input type="text" class="form-control" name="company" placeholder="Company"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="add-email" class="col-md-4 control-label">Email:</label>
                                <div class="col-md-8">
                                    <input type="email" class="form-control" name="email" placeholder="Email"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="add-phone" class="col-md-4 control-label">Phone:</label>
                                <div class="col-md-8">
                                    <input type="tel" class="form-control" name="phone" placeholder="Phone"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-offset-4 col-md-8">
                                    <input type="submit" class="btn btn-default" value="Create Contact"/>
                                </div>
                            </div>
                        </form>
                    </sec:authorize>
                </div> <!-- End col div -->

            </div> <!-- End row div -->
        </div>
        <!-- Placed at the end of the document so the pages load faster -->
        <script src="${pageContext.request.contextPath}/js/jquery-3.1.1.min.js"></script>
        <script src="${pageContext.request.contextPath}/js/bootstrap.min.js"></script>

    </body>
</html>
