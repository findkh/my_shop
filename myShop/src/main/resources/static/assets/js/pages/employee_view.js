$(document).ready(function() {
	
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
		console.log('목록 버튼 클릭')
	});
	
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
				let data = response.result;
				//값 세팅
				$('#name').val(data.user_name);
				if(data.jumin_num != ""){
					$('#jumin1').val(data.jumin_num);
					$('#jumin2').val('999999');
				}
				$('#tel').val(data.phone_number);
				$('#employeeCode').val(data.employee_code);
				$('#address').val(data.address);
				$('#accountNum').val(data.account_num);
				$('#employeeComments').val(data.comments);
				$('#joinDate').val(data.join_date);
				$('#hireDate').val(data.hire_date);
				$('#userStatus').val(data.user_status);
				$('#jobType').val(data.job_type);
				$('#startTime').val(data.start_time);
				$('#endTime').val(data.end_time);
				$('#wageType').val(data.wage_type);
				$('#wage').val(data.wage);
				
				let workingDays = data.working_day.split(',');
				$.each(workingDays, function(index, day) {
					let checkboxId = '#' + day + 'Checkbox';
					$(checkboxId).prop('checked', true);
				});
				
				//직원 코드 QR 생성
				if(data.employee_code != ''){
					let qrCodeDiv = $('#employeeCodeImg')[0];
					let qrCodeText = data.employee_code;
					let qrCode = new QRCode(qrCodeDiv, {
						text: qrCodeText,
						width: 200,
						height: 200
					});
				}
				
				if(data.employeeImg != ''){
					console.log('이미지 존재함');
					getEmployeeImg(data.employeeImg);
				}
			}
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
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