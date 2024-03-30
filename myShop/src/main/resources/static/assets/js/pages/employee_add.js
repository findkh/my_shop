$(document).ready(function() {
	let csrfParam = $('#csrfParam').val();
	
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
			tel: $('tel').val(),
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
			wage_type: $('#wageType').val()
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
			alert('사진 용량이 50KB를 초과할 수 없습니다.');
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

//근무 요일 생성 함수
function getCheckedValues() {
	let checkedValues = [];
	
	$('.form-check-input:checked').each(function() {
		checkedValues.push($(this).val());
	});
	
	return checkedValues.join(',');
};

//사진 용량 체크
function isOverSize(file) {
	let maxSize = 50 * 1024; // 50KB로 제한
	return (file.size > maxSize) ? true : false;
};

//확장자 확인
function isImageFile(file) {
	let ext = file.name.split(".").pop().toLowerCase();
	console.log(ext);
	return ($.inArray(ext, ["jpg", "jpeg", "gif", "png"]) == -1) ? true : false;
};

//직원 저장 함수
function saveEmployee(info, detail, img) {
	console.log('여기')
	console.log(csrfParam.value)
	
    let formData = new FormData();
    formData.append('info', JSON.stringify(info));
    formData.append('detail', JSON.stringify(detail));
    formData.append('_csrf', csrfParam.value)

    if (img != null && img != undefined) {
        formData.append('img', img);
    }

    // 쿠키에서 CSRF 토큰을 가져옵니다.
//    let csrfToken = getCsrfTokenFromCookie();
    

    $.ajax({
        url: '/employee/saveEmployee',
        type: 'POST',
        data: formData,
        processData: false,
        contentType: false,
//        beforeSend: function(xhr) {
//            // AJAX 요청 전에 CSRF 토큰을 헤더에 추가
//            xhr.setRequestHeader('X-CSRF-TOKEN', $('#csrfParameterName').val());
//        },
        success: function(response) {
            console.log(response);
            // 성공적으로 서버로부터 응답을 받았을 때의 동작을 정의합니다.
        },
        error: function(xhr, status, error) {
            console.error(xhr.responseText);
            // 요청이 실패했을 때의 동작을 정의합니다.
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