<%@ include file="partials/header.jsp"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="s" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="f"%>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Exam</title>

</head>


<body id="page-top">




<div id="wrapper">


    <div id="content-wrapper">

        <div class="container-fluid">

            <!-- Breadcrumbs-->
            <ol class="breadcrumb">
                <li class="breadcrumb-item">
                    <a href="#">${currentStudentModel.studentFirstname} ${currentStudentModel.studentLastname}</a>
                </li>
                <li class="breadcrumb-item active"><c:out value='${currentTestModel.testName}' /></li>
            </ol>

            <!-- Questions and answers -->

            <form action="<c:url value="/exam/processExam"/>" id="questionForm" method="POST">

                    <div class="qList">
                        <div class="jumbotron">
                            <h1 class="display-4" id="questiontext">${question.contentOfQuestion}</h1>
                            <hr class="my-4">
                            <div id="answersloop">
                            <c:forEach var="a" items="${answersModel}">
                                <label class="container">
                                <input type="radio" id="answerReference" value="${a.answerId}" > ${a.contentOfAnswer}
                                    <span class="checkmark"></span>
                                </label>
                            </c:forEach>
                            </div>
                        </div>
                    </div>
                <div hidden="hidden" id="submitDiv"><button class="btn btn-primary btn-lg btn-block" id="btnSubmit">Submit</button></div>            </form>
            <form id="nextQuestionForm" action="<c:url value="/exam/nextQuestion"/>" method="POST" >
                <input type="hidden" name="questionReference" id="questionReference" value="${question.questionId}"/>
                <input type="hidden" name="groupReference" id="groupReference" value="${currentGroupModel.groupId}">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                <button type="submit" class="btn btn-primary btn-lg " id="btnNext" style="width:50%">Next</button>
            </form>
            <a class="btn btn-link btn-lg " id="btnPrevious" href="#" style="width: 50%" >Previous</a>


        </div>
    </div>
</div>

<script type="text/javascript">

    var submit=true;

    $('#nextQuestionForm').on('submit', function(event) {
        if(submit === false) {
            event.preventDefault(); // if you want to disable the action
            return false;
        } else {
            return true;
        }
    });

    var validatorNextQuestion = $('#nextQuestionForm').validate({
        submitHandler: function(form) {
            console.log("********* submitHandler *********");
            $(form).ajaxSubmit({
                url: URLWithContextPath + "/exam/nextQuestion",
                dataType: "json",
                type: "post",
                success: function(data) {
                    console.log("********* AJAX CALL *********");
                    console.log("Status: " + data.status);
                    console.log("Result: " + data.result);
                    $("#questiontext").html(data.result.question);
                    $("#answersloop").html(data.result.resultsuccess);

                }
            });
        }
    });
</script>



<%@include file="partials/footer.jsp"%>
