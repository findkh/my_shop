$(document).ready(function() {
	let csrfParam = $('#csrfParam').val();
	
	//입사일 세팅
	let today = new Date();
	let dd = String(today.getDate()).padStart(2, '0');
	let mm = String(today.getMonth() + 1).padStart(2, '0');
	let yyyy = today.getFullYear();
	let formattedDate = yyyy + '-' + mm + '-' + dd;
	$('#joinDate').val(formattedDate);
	
	//근무시간 기본값 세팅
	let defaultStartTime = '09:00';
	let defaultEndTime = '18:00';

	$('#startTime').val(defaultStartTime);
	$('#endTime').val(defaultEndTime);
	
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
		saveEmployee(info, detail, imageFile);
	});
	
	//목록 이동 버튼
	$('#goListBtn').click(function(){
		window.location.href = '/employeeList';
	});
	
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
			$("#previewImg").attr("src", e.target.result);
		}
		reader.readAsDataURL(file);
	});
	
	//파트타임, 정직원 체인지 이벤트
	$('#jobType').on('change', function(){
		if($('#jobType').val() == 'full'){
			$('#wageType').val('year');
		} else {
			$('#wageType').val('time');
		}
	});
	
});

//직원 저장 함수
function saveEmployee(info, detail, img) {
	let formData = new FormData();
	formData.append('info', JSON.stringify(info));
	formData.append('detail', JSON.stringify(detail));
	formData.append('_csrf', csrfParam.value);

	if (img != null && img != undefined) {
		formData.append('img', img);
	}

	$.ajax({
		url: '/employee/saveEmployee',
		type: 'POST',
		data: formData,
		processData: false,
		contentType: false,
		success: function(response) {
			if(response.msg == 'success'){
				alert('저장되었습니다.');
				window.location.href = `viewEmployeeInfo?id=${response.id}`;
			}
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
}

function getCsrfTokenFromCookie() {
	let csrfToken = null;
	const cookieEntries = document.cookie.split(';');
	for (let i = 0; i < cookieEntries.length; i++) {
		const cookieEntry = cookieEntries[i].trim();
		if (cookieEntry.startsWith('XSRF-TOKEN=')) {
			csrfToken = cookieEntry.substring('XSRF-TOKEN='.length, cookieEntry.length);
			break;
		}
	}
	return csrfToken;
}