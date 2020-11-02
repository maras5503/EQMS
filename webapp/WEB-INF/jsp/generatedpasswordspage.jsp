<%@ include file="partials/header.jsp" %>

<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty allStudentsModel}">

                <div class="page-header" style="margin-top: 10px;">
                    <div class="col-sm-offset-3 col-sm-6" align="center">
                        <h1>Students and passwords</h1>
                    </div>
                    <div class="clearfix"></div>

                </div>

                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="students_passwords_table">
                        <thead>
                        <tr class="success">
                            <th>FIRSTNAME</th>
                            <th>LASTNAME</th>
                            <th>E-MAIL</th>
                            <th>PASSWORD</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach varStatus="loop" var="student" items="${allStudentsModel}">
                            <tr class="success" id="${student.studentId}">
                                <td><c:out value="${student.studentFirstname}" /></td>
                                <td><c:out value="${student.studentLastname}" /></td>
                                <td><c:out value="${student.studentEmail}" /></td>
                                <td><c:out value="${passwords.get(loop.index)}"/></td>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well">
                    There are no students in this group.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@ include file="partials/footer.jsp" %>