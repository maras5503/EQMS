<%@ include file="partials/header.jsp"%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Answers</title>

</head>

<body id="page-top">

<div id="wrapper">


    <div id="content-wrapper">

        <div class="container-fluid">

            <!-- Questions and answers -->

                <div class="qList">
                    <div class="page-header" style="margin-top: 10px;">
                        <div class="col-sm-offset-3 col-sm-6" align="center">
                            <h1>Results of test "${test.testName}" for student ${student.studentFirstname} ${student.studentLastname} from group "${group.groupName}"</h1>
                        </div>
                        <div class="clearfix"></div>
                    </div>

                        <c:forEach var="question" items="${questions}">
                        <div class="jumbotron">
                            <c:if test="${question.pictures.pictureId != null}">
                                <img src="<c:url value="/tests/image/${question.pictures.pictureId}"/>" alt="questionImage" name="questionImage" id="questionImage"/></label><br><br>
                            </c:if>
                        <h1 class="display-4" id="questiontext">${question.contentOfQuestion}</h1>
                        <hr class="my-4">
                        <div id="answersloop">
                            <c:forEach var="answer" items="${answers}">
                                <c:if test="${question.questionId == answer.key}">
                                    <c:forEach var="a" items="${answer.value}">
                                        <label class="container">
                                            <c:if test="${a.whetherCorrect}">
                                                ${a.contentOfAnswer} <span class="glyphicon glyphicon-ok" aria-hidden="true" style="color: green"></span>
                                                <c:forEach var="sa" items="${studentsAnswers}">
                                                    <c:if test="${sa == a.answerId}">
                                                        <b style="color: green">  Selected Answer</b>
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                            <c:if test="${!a.whetherCorrect}">
                                                ${a.contentOfAnswer}
                                                <c:forEach var="sa" items="${studentsAnswers}">
                                                    <c:if test="${sa == a.answerId}">
                                                        <span class="glyphicon glyphicon-remove" aria-hidden="true" style="color: red"></span>
                                                        <b style="color: red">  Selected Answer</b>
                                                    </c:if>
                                                </c:forEach>
                                            </c:if>
                                        </label>
                                        <c:if test="${a.pictures.pictureId != null}">
                                            <img src="<c:url value="/tests/image/${a.pictures.pictureId}"/>" alt="answerImage" name="answerImage" id="answerImage"/></label><br><br>
                                        </c:if>
                                    </c:forEach>
                                </c:if>
                            </c:forEach>
                        </div>
                        </div>
                        </c:forEach>
                    </div>
                </div>






        </div>
    </div>
</div>
</body>