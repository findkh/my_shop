$(document).ready(function() {
	let csrfParam = $('#csrfParam').val();
	let urlParams = new URLSearchParams(window.location.search);
	let pageNumber = urlParams.get('pageNumber') || 1;
	
	const quill = new Quill('#editor', {
		theme: 'snow'
	});
	
	$('#cancelBtn').click(function(){
		window.location.href = '/noticeList?pageNumber='+pageNumber;
	});
	
	$('#saveBtn').click(function() {
		const title = $('#title').val();
		const content = quill.root.innerHTML;
		
		if(title == ''){
			alert('제목을 입력해주세요.');
			return false;
		}
		
		if(content == ''){
			alert('내용을 입력해주세요.');
			return false;
		}
		
		saveNotice(title, content);
		
	});
});

function saveNotice(title, content) {
	let noticeData = {
		title: title,
		content: content
	};

	$.ajax({
		url: '/employee/saveNotice',
		type: 'POST',
		contentType: 'application/json',
		headers: { 'X-XSRF-TOKEN': csrfParam.value },
		data: JSON.stringify(noticeData),
		success: function(response) {
			if(response.msg == 'success'){
				alert('등록되었습니다.');
				if(response.id != null || response.id != ''){
					window.location.href = '/viewNotice?id='+response.id;
				}
			}
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
}