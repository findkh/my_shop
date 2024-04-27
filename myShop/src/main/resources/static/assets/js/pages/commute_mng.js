$(document).ready(function() {
	const employeeGrid = new tui.Grid({
		el: document.getElementById('employeeGrid'),
		data: [],
		scrollX: false,
		scrollY: true,
		rowHeaders: ['rowNum'],
		columns: [
			{ header: '구분', name: 'a'},
			{ header: '이름', name: 'user_name', className: 'column_click'},
			{ header: '재직상태', name: 'status_nm', className: 'column_click' },
		]
	});

	function onScanSuccess(qrCodeMessage) {
		$('#emplyeeCode').val(qrCodeMessage);
		$('#attendanceBtn').click();
	}

	function onScanError(errorMessage) {
		console.error(errorMessage);
	}

	let html5QrcodeScanner = new Html5QrcodeScanner(
		'qr-reader', { fps: 10, qrbox: 250 });

	html5QrcodeScanner.render(onScanSuccess, onScanError);

	$('#attendanceBtn').click(function(){

	})



});

