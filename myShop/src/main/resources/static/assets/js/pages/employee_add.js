$(document).ready(function() {
	$('#saveBtn').click(function(){
		console.log('저장 버튼 클릭')
	})
	
	$("#imageFile").on("change", function(event) {
		let file = event.target.files[0];
		console.log(file)
		
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
	
	//확장자 jpg jpeg png 파일만 가능하게
	
	
});

//사진 용량 체크
function isOverSize(file) {
	let maxSize = 50 * 1024; // 50KB로 제한
	return (file.size > maxSize) ? true : false;
}

//확장자 확인
function isImageFile(file) {
	let ext = file.name.split(".").pop().toLowerCase();
	console.log(ext);
	return ($.inArray(ext, ["jpg", "jpeg", "gif", "png"]) === -1) ? true : false;
}