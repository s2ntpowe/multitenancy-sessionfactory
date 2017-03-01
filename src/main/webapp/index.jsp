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
		<hr>
		<div id="studentTableDiv" style="display:none;text-alignment:center">
			<table class="table table-hover table-condensed table-striped table-bordered" id="studentTable">
				<thead>
					<tr>
						<th>ClassId</th>
						<th>ClassName</th>
						<th>Username</th>
						<th>First Name</th>
						<th>Last Name</th>
						<th>Email</th>
					</tr>
				</thead>
				<tbody>
				</tbody>
			</table>
		</div>
		<div></div>
	</div>
<script src="jquery-1.8.3.js"></script>
<script src="bootstrap/js/bootstrap.js"></script>
<script>
$(document).ready(function(){			
	$("#tenant1StudentsButton").on("click", function() {	
		setTenant("tenant1");
	 });
	$("#tenant2StudentsButton").on("click", function() {	
		setTenant("tenant2");
	 });
});
function setTenant(tenant){
	$.get('updateTenant.html',{tenantId:tenant},function(responseText) { 
		updateTable(tenant);
 	});
}
function updateTable(tenant){
	$('#tenantDisplay').html(tenant);
	
	$.post('getAllStudents.html',function(responseText) { 
        var objectJson =JSON.parse(responseText);
        var jsonArray = objectJson.classArray;
        $('#studentTable tbody').html('');
        for(var i=0;i<jsonArray.length;i++){
        	var rows = '<tr><td>' + jsonArray[i].classId   + '</td>' +
        				   '<td>' + jsonArray[i].className + '</td>' +
        				   '<td>' + jsonArray[i].username  + '</td>' +
        				   '<td>' + jsonArray[i].firstname + '</td>' +
        				   '<td>' + jsonArray[i].lastname  + '</td>' +
        				   '<td>' + jsonArray[i].email     + '</td></tr>';
        				   
        	$('#studentTable tbody').append(rows);
        }
        $('#studentTableDiv').css('display','inline');
 	});
	
}
</script>
</body>
</html>
