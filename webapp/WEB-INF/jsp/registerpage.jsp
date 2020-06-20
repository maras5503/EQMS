<%@ include file="partials/header.jsp" %>

<div class="row">
	<div class="col-sm-offset-3 col-sm-6 well">
		<legend align="center">Register</legend>
		<form class="form-horizontal" action="<c:url value="/auth/newUser"/>" method="post" id="registerform">
		  <div class="form-group">
		    <label for="firstname" class="col-md-2 control-label">Firstname</label>
		    <div class="col-md-10">
		      <input type="text" class="form-control" id="firstname" name="firstname" placeholder="Firstname" maxlength="20" required="required">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="lastname" class="col-md-2 control-label">Lastname</label>
		    <div class="col-md-10">
		      <input type="text" class="form-control" id="lastname" name="lastname" placeholder="Lastname" maxlength="30" required="required">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="email" class="col-md-2 control-label">Email</label>
		    <div class="col-md-10">
		      <!-- <div id="wrap_email" class="error"> -->
		        <input type="email" class="form-control" id="email" name="email" placeholder="Email address" maxlength="35" required="required"  data-toggle="popover_email" data-placement="right" data-html="true">
		      <!-- </div> -->
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="password" class="col-md-2 control-label">Password</label>
		    <div class="col-md-10">
		    	<div id="wrap_password">
		        	<input type="password" class="form-control" id="password" name="password" placeholder="Password" required="required" data-container="body" data-toggle="popover_password" data-placement="right" data-html="true">
		      	</div>
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="confirm_password" class="col-md-2 control-label" style="padding-top: 0px">Confirm password</label>
		    <div class="col-md-10">
		    	<div id="wrap_confirm_password">
		      		<input type="password" class="form-control" id="confirm_password" name="confirm_password" placeholder="Confirm password" required="required" data-container="#wrap_confirm_password" data-toggle="popover_confirm_password" data-placement="right" data-html="true">
		    	</div>
		    </div>
		  </div>
		  <div class="form-group">
		  	<div class="col-md-offset-2 col-md-7">
		  		<div id="wrap">
		  	  		<div class="g-recaptcha" id="recaptcha" data-sitekey="6LeHAgoTAAAAAARp7YnHqeKRY6509evvuvgTWrcj" data-callback="captcha_filled" data-expired-callback="captcha_expired" data-container="#wrap" data-toggle="popover" data-placement="right"></div>	  	  
		 		</div>
		 	</div>
		  </div>
		  <div class="form-group">
		    <div class="col-md-12">
		      <button type="submit" class="btn btn-md btn-block btn-primary">Register</button>
		      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		    </div>
		  </div>
		</form>
	</div>
</div> <!-- /row -->

<script type="text/javascript">
	var captcha_ok = false;
	var email_ok = false;
	var passwords_same = false;
	var correct_form_password = false;

	function captcha_filled() {
		captcha_ok = true;
		console.log("captcha_filled");
		console.log("captcha_ok = "+captcha_ok.toString());

		$('[data-toggle="popover"]').popover('destroy');
	}

	function captcha_expired() {
		captcha_ok = false;
		console.log("captcha_expired");
		console.log("captcha_ok = "+captcha_ok.toString());

		$('[data-toggle="popover"]').attr("data-title", "Captcha is expired");
		$('[data-toggle="popover"]').attr("data-content", "Please select the checkbox again");
		$('[data-toggle="popover"]').popover('show');
	}

	$("#password, #confirm_password").on('change click', function(){
		if( ($("#password").val() != "") && ($("#confirm_password").val() != "") ) {
			if (document.getElementById("password").value != document.getElementById("confirm_password").value) {
				passwords_same = false;
				console.log("passwords are different");
				console.log("password_same = "+passwords_same.toString());
				
				$('[data-toggle="popover_confirm_password"]').attr('data-title', "Passwords are different!");
				$('[data-toggle="popover_confirm_password"]').attr('data-content', "Please re-enter your password");
				$('[data-toggle="popover_confirm_password"]').popover('show');
				$("#confirm_password").parent().addClass('has-error');
			} else {
				passwords_same = true;
				console.log("passwords are same");
				console.log("password_same = "+passwords_same.toString());

				$('[data-toggle="popover_confirm_password"]').popover('destroy');
				$('#confirm_password').parent().removeClass('has-error');
			}
		} 

		$('[data-toggle="popover_email"]').popover('hide');
	    $('[data-toggle="popover_password"]').popover('hide');
	});
	
	$("#password").on('change click', function(){
		if($("#password").val() != "") {
			
			var data = {};
			data[csrfParameter] = csrfToken;
			data["password"] = document.getElementById("password").value;
	
			$.ajax({
				url: URLWithContextPath + "/auth/checkPasswordValidation",
				data: data,
				type: "POST",
				success: function(data){
					console.log(data);
					if(data.status == "FAIL"){
						correct_form_password = false;
						console.log("correct_form_password = "+correct_form_password.toString());
						
						$('[data-toggle="popover_password"]').attr('data-title', "The password does not meet the following requirements:");
						$('[data-toggle="popover_password"]').attr('data-content', data.result);
						$('[data-toggle="popover_password"]').popover('show');
						$("#password").parent().addClass('has-error');

					} else if(data.status == "SUCCESS") {
						correct_form_password = true;
						console.log("correct_form_password = "+correct_form_password.toString());
						
						console.log(data.result);
						$('[data-toggle="popover_password"]').popover('destroy');
						$('#password').parent().removeClass('has-error');
					}
				}
			});
		} else {
			console.log("password input field is empty");
			$('[data-toggle="popover_password"]').popover('destroy');
		}
		
		$('[data-toggle="popover_email"]').popover('hide');
	    $('[data-toggle="popover_confirm_password"]').popover('hide');
	});

	$("#email").on('change click', function(){
	
		var data = {};
		data[csrfParameter] = csrfToken;
		data["email"] = document.getElementById("email").value;
		
		if($(this).val() != "") {
			$.ajax({
			url: URLWithContextPath + "/auth/registerCheck",
			data: data,
			type: "POST",
			success: function(data){
				console.log(data);
				if(data == "FAIL"){
					email_ok = false;
					console.log("email_ok = "+email_ok.toString());
					
					$('[data-toggle="popover_email"]').attr('data-title', "The given e-mail address already exists!");
					$('[data-toggle="popover_email"]').attr('data-content', "Please enter a different e-mail address");
					$('[data-toggle="popover_email"]').popover('show');
					$("#email").parent().addClass('has-error');
					
				} else if(data == "SUCCESS") {
					email_ok = true;
					console.log("email_ok = "+email_ok.toString());
					
					$('[data-toggle="popover_email"]').popover('destroy');
					$('#email').parent().removeClass('has-error');
				}
			}
			});
		} else {
			console.log("email input field is empty");
			$('[data-toggle="popover_email"]').popover('destroy');
		}

		$('[data-toggle="popover_password"]').popover('hide');
		$('[data-toggle="popover_confirm_password"]').popover('hide');
	});
	
	$('#registerform').on('submit', function(event) {
		if((captcha_ok === false) || (passwords_same === false) || (correct_form_password === false) || (email_ok === false)) {
			if(captcha_ok === false) {
				$('[data-toggle="popover"]').attr("data-title", "Captcha is not filled");
				$('[data-toggle="popover"]').attr("data-content", "Please select the checkbox");
				$('[data-toggle="popover"]').popover('show');
			} else {
				$('[data-toggle="popover"]').popover('destroy');
			}

			event.preventDefault(); // if you want to disable the action
			return false;
		} else {
			return true;
		}
	});
</script>


<%@ include file="partials/footer.jsp" %>