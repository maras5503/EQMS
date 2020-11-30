<%@include file="partials/header.jsp"%>

<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty students}">

                <div class="page-header" style="margin-top: 10px;">
                <div class="col-sm-offset-3 col-sm-6" align="center">
                    <h1>Results of the test "${conductedexam.testName}" for group "${studentgroup.studentgroupName}"</h1>
                </div>
                <div class="clearfix"></div>
            </div>
                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="conducted_exams_table">
                        <thead>
                        <tr class="success">
                            <th>FIRSTNAME</th>
                            <th>LASTNAME</th>
                            <th>MARK</th>
                            <th>SCORE</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach varStatus="loop" var="student" items="${students}">
                            <tr class="success" id="${student.studentId}">
                                <td><c:out value="${student.studentFirstname}" /></td>
                                <td><c:out value="${student.studentLastname}" /></td>
                                <td>
                                    <c:forEach var="mark" items="${exammarks}">
                                        <c:if test="${mark.key == student.studentId}">
                                            <c:out value="${mark.value}"/>
                                        </c:if>
                                    </c:forEach>
                                </td>
                                <td>
                                    <c:forEach var="score" items="${examresults}">
                                        <c:if test="${score.key == student.studentId}">
                                            <c:out value="${score.value} / ${group.numberOfQuestions}"/>
                                        </c:if>
                                    </c:forEach>

                                </td>
                                <td>
                                    <form action="<c:url value="/history/answers"/>" method="POST"   id="answersForm">
                                        <input type="hidden" name="studentGroupReference" value="${studentgroup.studentgroupId}"/>
                                        <input type="hidden" name="studentReference" value="${student.studentId}"/>
                                        <input type="hidden" name="conductedExamReference" value="${conductedexam.conductedExamId}" />
                                        <input type="hidden" name="groupReference" value="${group.groupId}"/>
                                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                                        <button type="submit" class="btn btn-info btn-block btn-sm" id="answersBtn" name="answersBtn"><span class="glyphicon glyphicon-menu-hamburger" aria-hidden="true"></span> Answers</button>
                                    </form>
                                </td>
                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well">
                    Group is empty
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<%@include file="partials/footer.jsp"%>