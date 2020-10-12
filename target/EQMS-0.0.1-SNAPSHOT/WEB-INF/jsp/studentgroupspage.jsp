<%@ include file="partials/header.jsp" %>
<div class="row">

    <div class="col-sm-12">

        <c:choose>
            <c:when test="${not empty allStudentGroupsModel}">

                <div class="page-header" style="margin-top: 10px;">
                    <div class="col-sm-offset-3 col-sm-6" align="center">
                        <h1>All groups</h1>
                    </div>

                    <div id="addStudentGroup" class="pull-right" >
                        <button type="button" class="btn btn-success" id="addStudentGroupBtn" name="addStudentGroupBtn" style="margin-top: 20px; margin-bottom: 10px;" data-toggle="modal" data-target="#addStudenGroupModal"><span class="glyphicon glyphicon-plus" aria-hidden="true"></span> Add new group</button>
                    </div>

                    <div class="clearfix"></div>
                </div>

                <!-- try http://getbootstrap.com/css/#tables-responsive -->
                <div class="table-responsive" style="margin-bottom: 0px">
                    <table class="table table-bordered" id="studentgroups_table">
                        <thead>
                        <tr class="success">
                            <th style="width: 50%;">GROUP NAME</th>
                            <th style="border-right-width: 0px;"></th>
                            <th style="border-right-width: 0px;"></th>
                            <th style="border-right-width: 0px;"></th> <!-- style="empty-cells: hide" -->
                        </tr>
                        </thead>
                        <tbody>
                        <!-- Auxiliary value that determines whether the current object belongs to a given user -->

                        <c:forEach var="studentgroup" items="${allStudentGroupsModel}">

                            <tr class="success" id="${studentgroup.studentGroupId}">
                                <td><c:out value="${studentgroup.studentGroupName}" /></td>

                                    <td>
                                        <button type="button" class="btn btn-info btn-block btn-sm" id="editStudentGroupBtn" name="editStudentGroupBtn" data-toggle="modal" data-target="#editStudentGroupModal" data-studentgroup-name="${studentgroup.studentGroupName}" data-studentgroup-reference="${studentgroup.studentGroupId}"><span class="glyphicon glyphicon-edit" aria-hidden="true"></span> Edit name</button>
                                    </td>
                                    <td>
                                        <button type="button" class="btn btn-danger btn-block btn-sm" id="deleteStudentGroupBtn" name="deleteStudentGroupBtn" data-toggle="modal" data-target="#confirmDeleteStudentGroup" data-title="Delete Group" data-message="Are you sure you want to delete group '${studentgroup.studentGroupName}'?" data-studentgroup-reference="${studentgroup.studentGroupId}"><span class="glyphicon glyphicon-remove" aria-hidden="true"></span> Delete</button>
                                    </td>

                            </tr>

                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </c:when>
            <c:otherwise>
                <div class="well">
                    There are no groups.
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</div>

<div class="modal fade" id="addStudentGroupModal" tabindex="-1" role="dialog" aria-labelledby="addStudentGroupLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="addStudentGroupTitleModal">Add new group</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/studentgroups/doAddStudentGroup"/>" method="POST" id="addStudentGroupFormModal">
                    <div class="form-group">
                        <label for="studentGroupNameModal" class="control-label">Name:</label>
                        <input type="text" class="form-control" id="studentGroupNameModal" name="studentGroupNameModal" placeholder="Group name" maxlength="100" aria-describedby="inputAddStudentGroupError">
                        <span id="glyphiconErrorAddStudentGroup" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputAddStudentGroupError" class="sr-only">(error)</span>
                    </div>
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="addStudentGroupBtnModal">Add group</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="editStudentGroupModal" tabindex="-1" role="dialog" aria-labelledby="editStudentGroupLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="editStudentGroupTitleModal">Edit group</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/studentgroups/doEditStudentGroup"/>" method="POST" id="editStudentGroupFormModal">
                    <div class="form-group">
                        <label for="studentGroupNameModal" class="control-label">Group:</label>
                        <input type="text" class="form-control" id="studentGroupNameModal" name="studentGroupNameModal" placeholder="Group name" maxlength="100" aria-describedby="inputEditStudentGroupError">
                        <span id="glyphiconErrorEditStudentGroup" class="form-control-feedback" aria-hidden="true"></span>
                        <span id="inputEditStudentGroupError" class="sr-only">(error)</span>
                    </div>
                    <input type="hidden" name="studentGroupReference" id="studentGroupReference" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-primary" id="editStudentGroupBtnModal">Edit group</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="confirmDeleteStudentGroup" tabindex="-1" role="dialog" aria-labelledby="confirmDeleteStudentGroupLabelModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="confirmDeleteStudentGroupTitleModal">Delete group</h4>
            </div>
            <div class="modal-body">
                <form action="<c:url value="/studentgroups/doDeleteStudentGroup"/>" method="POST" id="deleteStudentGroupFormModal" >
                    <div class="form-group" id="confirmDeleteStudentGroupLabel">
                    </div>
                    <input type="hidden" name="studentGroupReference" id="studentGroupReference" />
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default" data-dismiss="modal">Close</button>
                <button type="button" class="btn btn-danger" id="confirmDeleteStudentGroupBtnModal">Delete</button>
            </div>
        </div>
    </div>
</div>

