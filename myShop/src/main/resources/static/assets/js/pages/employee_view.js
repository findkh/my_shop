$(document).ready(function() {
	let originData = '';
	const urlParams = new URLSearchParams(window.location.search);
	const id = urlParams.get('id');

	if (!id) {
		alert('잘못된 접근입니다.');
		window.location.href = '/employeeList';
	} else {
		getEmployeeInfo(id);
	}
	
	$('#goListBtn').click(function(){
		window.location.href = '/employeeList';
	});
	
	$('#goEditBtn').click(function(){
		$('#imgUploadInput').css('display', '');
		$('#saveBtn').css('display', '');
		$('#cancelBtn').css('display', '');
		$('#goEditBtn').css('display', 'none');
		$("#name").removeAttr("disabled");
		$("#jumin1").removeAttr("disabled");
		$("#jumin2").removeAttr("disabled");
		$("#tel").removeAttr("disabled");
		$("#address").removeAttr("disabled");
		$("#accountNum").removeAttr("disabled");
		$("#employeeComments").removeAttr("disabled");
		$("#joinDate").removeAttr("disabled");
		$("#hireDate").removeAttr("disabled");
		$("#userStatus").removeAttr("disabled");
		$("#jobType").removeAttr("disabled");
		$("#startTime").removeAttr("disabled");
		$("#endTime").removeAttr("disabled");
		$("#wageType").removeAttr("disabled");
		$("#wage").removeAttr("disabled");
		$(".form-check-input").removeAttr("disabled");
	});
	
	$('#cancelBtn').click(function(){
		$('#imgUploadInput').css('display', 'none');
		$('#saveBtn').css('display', 'none');
		$('#cancelBtn').css('display', 'none');
		$('#goEditBtn').css('display', '');
		$("#name").prop("disabled", true);
		$("#jumin1").prop("disabled", true);
		$("#jumin2").prop("disabled", true);
		$("#tel").prop("disabled", true);
		$("#address").prop("disabled", true);
		$("#accountNum").prop("disabled", true);
		$("#employeeComments").prop("disabled", true);
		$("#joinDate").prop("disabled", true);
		$("#hireDate").prop("disabled", true);
		$("#userStatus").prop("disabled", true);
		$("#jobType").prop("disabled", true);
		$("#startTime").prop("disabled", true);
		$("#endTime").prop("disabled", true);
		$("#wageType").prop("disabled", true);
		$("#wage").prop("disabled", true);
		$(".form-check-input").prop("disabled", true);
		fillData();
	})
	
	//이미지 업로드 체인지 이벤트
	$('#imageFile').on('change', function(event) {
		let file = event.target.files[0];
		
		if(isImageFile(file)){
			alert('jpg, jpeg, gif, png 확장자만 사용할 수 있습니다.');
			$("#imageFile").val('');
			return false;
		}
		
		if(isOverSize(file)){
			alert('사진 용량이 100MB를 초과할 수 없습니다.');
			$("#imageFile").val('');
			return false;
		}
		
		var reader = new FileReader(); 
		reader.onload = function(e) {
			$("#employeeImg").attr("src", e.target.result);
		}
		reader.readAsDataURL(file);
	});
	
	
	$('#saveBtn').click(function(){
		//직원정보 필수값 유효성 검사
		//필수값: 이름, 전화번호
		if($('#name').val() == ''){
			alert('이름은 필수값입니다.');
			return false;
		}
		
		if($('#tel').val() == ''){
			alert('이름은 필수값입니다.');
			return false;
		}
		
		let info = {
			name: $('#name').val(),
			jumin_num: $('#jumin1').val() + '-' + $('#jumin2').val(),
			tel: $('#tel').val(),
			address: $('#address').val(),
			account_num: $('#accountNum').val(),
			comments: $('#employeeComments').val(),
		};
		
		let detail = {
			join_date: $('#joinDate').val(),
			hire_date: $('#hireDate').val(),
			user_status: $('#userStatus').val(),
			job_type: $('#jobType').val(),
			working_day: getCheckedValues(),
			start_time: $('#startTime').val(),
			end_time: $('#endTime').val(),
			wage_type: $('#wageType').val(),
			wage: $('#wage').val() == "" ? 0 : $('#wage').val()
		};
		
		let imageFile = $('#imageFile')[0].files[0];
		
		console.log(info)
		console.log(detail)
		//saveEmployee(info, detail, imageFile);
	});
	
	$('#checkPwd').click(function(){
		console.log('주민번호 클릭')
	})
});

function getEmployeeInfo(id) {
	$.ajax({
		url: '/getEmployeeInfo',
		type: 'GET',
		data: { id: id },
		success: function(response) {
			if(response.result == 'invalid'){
				alert('존재하지 않는 사용자 입니다.');
				window.location.href = '/employeeList';
			} else {
				originData = response.result;
				fillData();
			}
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
}

function fillData(){
	//값 세팅
	$('#name').val(originData.user_name);
	if(originData.jumin_num != ""){
		$('#jumin1').val(originData.jumin_num1);
		$('#jumin2').val(originData.jumin_num2);
	}
	$('#tel').val(originData.phone_number);
	$('#employeeCode').val(originData.employee_code);
	$('#address').val(originData.address);
	$('#accountNum').val(originData.account_num);
	$('#employeeComments').val(originData.comments);
	$('#joinDate').val(originData.join_date);
	$('#hireDate').val(originData.hire_date);
	$('#userStatus').val(originData.user_status);
	$('#jobType').val(originData.job_type);
	$('#startTime').val(originData.start_time);
	$('#endTime').val(originData.end_time);
	$('#wageType').val(originData.wage_type);
	$('#wage').val(originData.wage);
	
	let workingDays = originData.working_day.split(',');
	$.each(workingDays, function(index, day) {
		let checkboxId = '#' + day + 'Checkbox';
		$(checkboxId).prop('checked', true);
	});
	
	//직원 코드 QR 생성
	if(originData.employee_code != ''){
		$('#employeeCodeImg').empty();
		let qrCodeDiv = $('#employeeCodeImg')[0];
		let qrCodeText = originData.employee_code;
		let qrCode = new QRCode(qrCodeDiv, {
			text: qrCodeText,
			width: 200,
			height: 200
		});
	}
	
	if(originData.employeeImg != ''){
		getEmployeeImg(originData.employeeImg);
	}
}

function getEmployeeImg(img) {
	$.ajax({
		url: '/getEmployeeImg',
		type: 'GET',
		data: { fileName: img},
		xhrFields: {
			responseType: 'blob'
		},
		success: function(response) {
			let blobUrl = URL.createObjectURL(response);
			let imgElement = document.getElementById('employeeImg');
			imgElement.src = blobUrl;
		}
	});
}