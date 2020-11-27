<%@ include file="partials/header.jsp"%>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Results of test ${test.testName} for student ${student.studentFirstname} ${student.studentLastname} from group ${group.groupName}</title>

</head>

<body id="page-top">

<div id="wrapper">


    <div id="content-wrapper">

        <div class="container-fluid">

            <!-- Questions and answers -->

                <div class="qList">
                    <div align="center" id="questionImageDiv">
                        ${image} <br><br>
                    </div>

                    <div class="jumbotron">
                        <c:forEach var="question" items="${questions}">
                        <h1 class="display-4" id="questiontext">${question.contentOfQuestion}</h1>
                        <hr class="my-4">
                        <div id="answersloop">
                            <c:forEach var="answer" items="${answers}">
                                <c:if test="${question.questionId == answer.questionId}">
                                        <label class="container">
                                        ${answer.contentOfAnswer}
                                        </label>
                                        <c:if test="${answer.pictures.pictureId != null}">
                                            <img src="<c:out value="/tests/image/${answer.pictures.pictureId}"/>" alt="questionImage" name="questionImage" id="questionImage"/></label><br><br>
                                        </c:if>
                                </c:if>
                            </c:forEach>
                        </div>
                        </c:forEach>
                    </div>
                </div>






        </div>
    </div>
</div>
</body>