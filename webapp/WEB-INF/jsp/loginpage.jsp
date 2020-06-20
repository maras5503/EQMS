<%@ include file="partials/header.jsp" %>

<div class="row">
	<div class="col-sm-offset-3 col-sm-6 well">
		<legend align="center">Login</legend>
		<form class="form-horizontal" action="../j_spring_security_check" method="post" id="loginform">
		  <div class="form-group">
		    <label for="j_username" class="col-md-2 control-label">Email</label>
		    <div class="col-md-10">
		      <input type="email" class="form-control" id="j_username" name="j_username" placeholder="Email" required="required">
		    </div>
		  </div>
		  <div class="form-group">
		    <label for="j_password" class="col-md-2 control-label">Password</label>
		    <div class="col-md-10">
		      <input type="password" class="form-control" id="j_password" name="j_password" placeholder="Password" required="required">
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
		    <div class="col-md-offset-2 col-md-10">
		      <div class="checkbox">
		        <label>
		          <input type="checkbox" id="remember-me" name="remember-me"> Remember me
		        </label>
		      </div>
		    </div>
		  </div>
		  <div class="form-group">
		    <div class="col-md-12">
		      <button type="submit" class="btn btn-md btn-block btn-primary">Sign in</button>
		      <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
		    </div>
		  </div>
		</form>
		<!-- 
		<script src="https://www.google.com/recaptcha/api.js?onload=onloadCallback&render=explicit"
          async defer>
    	</script>
    	 -->
	</div>
</div> <!-- /row -->


<script type="text/javascript">
	var submit = true;		// was false

	function captcha_filled() {
		submit = true;
		console.log("captcha_filled");
		console.log(submit.toString());
		
		$('[data-toggle="popover"]').popover('destroy');
	}

	function captcha_expired() {
		submit = false;
		console.log("captcha_expired");
		console.log(submit.toString());

		$('[data-toggle="popover"]').attr("data-title", "Captcha is expired");
		$('[data-toggle="popover"]').attr("data-content", "Please select the checkbox again");
		$('[data-toggle="popover"]').popover('show');
	}
	
	$('#loginform').on('submit', function(event) {
		if(submit === false) {
			$('[data-toggle="popover"]').attr("data-title", "Captcha is not filled");
			$('[data-toggle="popover"]').attr("data-content", "Please select the checkbox");
			$('[data-toggle="popover"]').popover('show');
			event.preventDefault(); // if you want to disable the action
			return false;
		} else {
			$('[data-toggle="popover"]').popover('destroy');
			return true;
		}
	});
</script>


<%@ include file="partials/footer.jsp" %>