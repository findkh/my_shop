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
	let maxSize = 100 * 1024 * 1024; // 100MB를 바이트 단위로 계산
	console.log(file.size > maxSize)
	console.log(file.size, maxSize)
	return (file.size > maxSize) ? true : false;
};

//확장자 확인
function isImageFile(file) {
	let ext = file.name.split(".").pop().toLowerCase();
	return ($.inArray(ext, ["jpg", "jpeg", "gif", "png"]) == -1) ? true : false;
};