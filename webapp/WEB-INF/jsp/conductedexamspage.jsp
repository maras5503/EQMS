<%@include file="partials/header.jsp"%>


<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty allConductedExams}">

                <div class="page-header" style="margin-top: 10px;">
                    <div class="col-sm-offset-3 col-sm-6" align="center">
                        <h1>History of conducted exams</h1>
                    </div>
                    <div class="clearfix"></div>
                </div>
                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="conducted_exams_table">
                        <thead>
                        <tr class="success">
                            <th>TEST NAME</th>
                            <th>SUBJECT NAME</th>
                            <th>STUDENT GROUP</th>
                            <th>DATE</th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach varStatus="loop" var="exam" items="${allConductedExams}">
                            <tr class="success" id="${exam.conductedExamId}">
                                <td><c:out value="${exam.testName}" /></td>
                                <td><c:out value="${student.studentLastname}" /></td>
                                <td><c:out value="${student.studentEmail}" /></td>
                                <td><c:out value="${passwordsModel.get(loop.index)}"/></td>
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

<%@include file="partials/footer.jsp"%>