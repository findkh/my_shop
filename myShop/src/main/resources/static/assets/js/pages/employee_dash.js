$(document).ready(function() {
	getDashBoardInfoByEmployee();
	getClock(); //맨처음에 한번 실행
	setInterval(getClock, 1000); //1초 주기로 새로실행
	
});


function getClock() {
	const clock = $('#dashboardDate');
	const d = new Date();
	const year = String(d.getFullYear());
	const month = String(d.getMonth() + 1).padStart(2, "0");
	const day = String(d.getDate()).padStart(2, "0");
	const h = String(d.getHours()).padStart(2, "0");
	const m = String(d.getMinutes()).padStart(2, "0");
	const s = String(d.getSeconds()).padStart(2, "0");
	clock.text(`${year}-${month}-${day} ${h}:${m}:${s}`);
}
	
function getDashBoardInfoByEmployee() {
	$.ajax({
		url: '/getDashBoardInfoByEmployee',
		type: 'GET',
		success: function(response) {
			
			console.log(response)
			
			if(response.userInfo.userName != undefined){
				$('#userName').html(response.userInfo.userName);
			}
			
			if(response.code.employee_code != undefined){
				$('#qrCodeDiv').empty();
				let qrCodeDiv = $('#qrCodeDiv')[0];
				let qrCodeText = response.code.employee_code;
				let qrCode = new QRCode(qrCodeDiv, {
					text: qrCodeText,
					width: 300,
					height: 300
				});
			}
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
}