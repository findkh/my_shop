$(document).ready(function() {
	let originData = '';
	
	getEmployeeInfo()
	
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
	
	$('#getJuminNum').click(function(){
		if($('#pwd1').val() != $('#pwd2').val()){
			alert('비밀번호가 일치하지 않습니다. 다시 입력해주세요.');
			return false;
		} else {
			getJuminNum($('#pwd1').val());
		}
	});
	
	$('#cancelModal').click(function(){
		$('#pwd1').val('');
		$('#pwd2').val('');
	});
	
});

function getEmployeeInfo() {
	$.ajax({
		url: '/getEmployeeInfoByEmployee',
		type: 'GET',
		success: function(response) {
			if(response.result == 'invalid'){
				alert('존재하지 않는 사용자 입니다.');
				window.location.href = '/login';
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
	$('#joinUrl').val(originData.joinUrl);
	
	let workingDays = originData.working_day.split(',');
	$.each(workingDays, function(index, day) {
		let checkboxId = '#' + day + 'Checkbox';
		$(checkboxId).prop('checked', true);
	});
	
	if(originData.employeeImg != '' && originData.employeeImg != undefined){
		getEmployeeImg(originData.employeeImg);
	}
}

//이미지 요청 함수
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

//비밀번호 체크 확인 함수
function getJuminNum(juminNum) {
	$.ajax({
		url: '/common/getJuminNum',
		type: 'POST',
		contentType: 'application/json',
		data: JSON.stringify({ 'password': juminNum }),
		headers: { 'X-XSRF-TOKEN': csrfParam.value },
		success: function(response) {
			if(response){
				alert('확인 되었습니다.');
				 $('#jumin2').prop('type', 'text');
				$('#cancelModal').click();
			} else {
				alert('비밀번호가 틀렸습니다. 다시 시도해주세요.');
				$('#pwd1').val('');
				$('#pwd2').val('');
			}
		},
		error: function(xhr, status, error) {
			alert('Error: ' + error.message);
		}
	});
}
