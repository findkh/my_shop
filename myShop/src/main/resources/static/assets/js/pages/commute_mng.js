$(document).ready(function() {
	const commuteGrid = new tui.Grid({
		el: document.getElementById('commuteGrid'),
		data: [],
		scrollX: false,
		scrollY: true,
		rowHeaders: ['rowNum'],
		columns: [
			{ header: '구분', name: 'commute_type'},
			{ header: '이름', name: 'user_name'},
			{ header: '시간', name: 'checked_time'},
		]
	});
	
	function onScanSuccess(qrCodeMessage) {
		$('#emplyeeCode').val(qrCodeMessage);
		if(qrCodeMessage != ''){
			$('#attendanceBtn').click();
		}
		
	}

	function onScanError(errorMessage) {
		console.error(errorMessage);
	}

	let html5QrcodeScanner = new Html5QrcodeScanner(
		'qr-reader', { fps: 10, qrbox: 250 });

	html5QrcodeScanner.render(onScanSuccess, onScanError);
	
	$('img[src^="data:image/svg+xml"]').remove();

	$('#attendanceBtn').click(function(){
		let commuteData = {
			type_str: $('#typeStr').val(),
			qrcode: $('#emplyeeCode').val(),
		};
		
		if($('#emplyeeCode').val() == ''){
			alert('직원코드를 입력해주세요.');
			return false;
		}
	
		$.ajax({
			url: '/employee/saveCommute',
			type: 'POST',
			contentType: 'application/json',
			headers: { 'X-XSRF-TOKEN': csrfParam.value },
			data: JSON.stringify(commuteData), // Serialize data to JSON
			success: function(response) {
				if(response.msg == 'success'){
					$('#emplyeeCode').val('');
					getCommuteList();
				} else if(response.msg == 'fail'){
					alert('처리 실패')
				} else {
					$('#emplyeeCode').val('');
				}
			},
			error: function(xhr, status, error) {
				console.error(xhr.responseText);
			}
		});
	});

	function getCommuteList(){
		$.ajax({
			url: '/getCommuteList',
			type: 'GET',
			success: function(response) {
				if(response.length > 0){
						commuteGrid.resetData(response);
					}
			},
			error: function(xhr, status, error) {
				console.error(xhr.responseText);
			}
		});
	}

	getCommuteList();
});

