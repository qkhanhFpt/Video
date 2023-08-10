<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="/common/taglib.jsp"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Forgot Password</title>

<%@ include file="/common/head.jsp"%>

</head>
<body>

	<%@ include file="/common/header.jsp"%>

	<div class="container-fluid tm-mt-60">
		<div class="row tm-mb-50">
			<div class="col-lg-12 col-12 mb-5">
				<center>
					<h2 class="tm-text-primary mb-5">Forgot Password</h2>
				</center>
				<div class="tm-contact-form mx-auto">
				<div class="form-group">
					<input type="email" name="email" id="email"
						class="form-control rounded-0" placeholder="Email" required />
				</div>
				<div class="form-group tm-text-right">
					<button type="submit" id="sendBtn" class="btn btn-primary">Send</button>
				</div>
				<h5 style="color: red" id="messageReturn"></h5>
				</div>
			</div>
		</div>
	</div>
	<!-- container-fluid, tm-container-content -->

	<%@ include file="/common/footer.jsp"%>

</body>

<script>
$('#sendBtn').click(function(){
	$('#messageReturn').text('');
	var email =  $('#email').val();
	var formData = {'email': email};
	$.ajax({
		url: 'forgotPass',
		type: 'POST',
		data: formData
	}).then(function(data){
		$('#messageReturn').text('Mật khẩu đã được đặt lại, kiểm tra email của bạn.');
		setTimeout(function(){
			window.location.href = 'http://localhost:8080/ASMJAVA4';			
		}, 5*1000);
	}).fail(function(error){	
		$('#messageReturn').text('Thông tin của bạn không chính xác, hãy thử lại!!!');
	});
});
</script>
</html>