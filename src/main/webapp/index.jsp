<html>
<head>
<link href="assets/css/bootstrap-united.css" rel="stylesheet" />
<link href="bootstrap/css/bootstrap-responsive.css" rel="stylesheet" />
<style>
body {
	height: 100%;
	margin: 0;
	display: compact;
}
</style>
</head>
<body>
	<div class="navbar navbar-default">

		<div class="navbar-header">
			<button type="button" class="navbar-toggle" data-toggle="collapse"
				data-target=".navbar-responsive-collapse">
				<span class="icon-bar"></span> <span class="icon-bar"></span> <span
					class="icon-bar"></span>
			</button>
		</div>

		<div class="navbar-collapse collapse navbar-responsive-collapse">
			<form class="navbar-form navbar-right">
				<input type="text" class="form-control" placeholder="Search">
			</form>
			<ul class="nav navbar-nav navbar-right">
				<li class="active"><a href="#">Home</a></li>
				<li><a href="signup.html">Signup</a></li>
				<li><a href="login.html">Login</a></li>
				<li class="dropdown"><a href="#" class="dropdown-toggle"
					data-toggle="dropdown">Explore<b class="caret"></b></a>
					<ul class="dropdown-menu">
						<li><a href="#">Contact us</a></li>
						<li class="divider"></li>
						<li><a href="#">Further Actions</a></li>
					</ul></li>
			</ul>
		</div>
		<!-- /.nav-collapse -->
	</div>
	<div class="container">
		<div class="jumbotron">
			<div>
				<h1>Welcome to Online Student Enrollment!</h1>
				<p>This project is an example implementation of Multi-Tenancy with Hibernate and Spring.</p>
			</div>
			<a class="btn btn-primary" href="signup.html">Signup » </a> 
			<a class="btn btn-primary" href="login.html">Login » </a>
			<a class="btn btn-primary" href="#" id="tenant1StudentsButton">View Students Tenant1 » </a>
			<a class="btn btn-primary" href="#" id="tenant2StudentsButton">View Students Tenant2 » </a>
		</div>
		<div></div>
	</div>
<script src="jquery-1.8.3.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script>
$(document).ready(function(){			
	$("#tenant1StudentsButton").on("click", function() {	
		 $.get('getAllStudents.html',{tenantId:"tenant1"},function(responseText) { 
	            var returnedText = responseText;
	            alert(returnedText);
	     });
	 });
	$("#tenant2StudentsButton").on("click", function() {	
		 $.get('getAllStudents.html',{tenantId:"tenant2"},function(responseText) { 
	            var returnedText = responseText;
	            alert(returnedText);
	     });
	 });
});
</script>
</body>
</html>
