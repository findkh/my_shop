$(document).ready(function() {
	let urlParams = new URLSearchParams(window.location.search);
	let pageNumber = urlParams.get('pageNumber') || 1; // 쿼리스트링이 없으면 1로 설정
	
	const quill = new Quill('#editor', {
		theme: 'snow',
		readOnly: true,
		modules: {
			toolbar: false // 툴바 숨기기
		}
	});
	
	if(urlParams.get('id') != '' || urlParams.get('id') != null){
		$.ajax({
			url: '/getNoticeDesc',
			type: 'GET',
			data: {
				id: urlParams.get('id')
			},
			success: function(data) {
				if(data == ''){
					alert('잘못된 접근입니다.');
					window.location.href = '/noticeList?&pageNumber='+pageNumber;
					return false;
				} else {
					if(data.flag == 'true'){
						$('#goEditBtn').show();
						$('#deleteBtn').show();
					}
					$('#title').val(data.title);
					$('#name').val(data.name);
					$('#created_dt').val(formatDate(data.created_dt))
					quill.clipboard.dangerouslyPasteHTML(data.content);
				}
			},
			error: function(xhr, status, error) {
				console.error(error);
			}
		});		
	}
	
	$('#goListBtn').click(function(){
		window.location.href = '/noticeList?&pageNumber='+pageNumber;
	});
	
	$('#goEditBtn').click(function(){
		window.location.href = '/editNotice?&id='+urlParams.get('id')+'&pageNumber='+pageNumber;
	});
	
	$('#deleteBtn').click(function(){
		let noticeData = {
			id: urlParams.get('id')
		};
	
		$.ajax({
			url: '/employee/deleteNotice',
			type: 'POST',
			contentType: 'application/json',
			headers: { 'X-XSRF-TOKEN': csrfParam.value },
			data: JSON.stringify(noticeData),
			success: function(response) {
				if(response.msg == 'success'){
					alert('삭제되었습니다.');
					window.location.href = '/noticeList?&pageNumber='+pageNumber;
				}
			},
			error: function(xhr, status, error) {
				console.error(xhr.responseText);
			}
		});
		
	})
});

function formatDate(dateString) {
	let options = {
		timeZone: 'Asia/Seoul',
		year: 'numeric',
		month: '2-digit',
		day: '2-digit',
		hour: '2-digit',
		minute: '2-digit',
		second: '2-digit'
	};
	let date = new Date(dateString);
	return new Intl.DateTimeFormat('ko-KR', options).format(date);
}
