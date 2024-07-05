$(document).ready(function() {
	getClock(); //맨처음에 한번 실행
	setInterval(getClock, 1000); //1초 주기로 새로실행
	getDashBoardInfo();
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

function getDashBoardInfo() {
	$.ajax({
		url: '/getDashBoardInfo',
		type: 'GET',
		success: function(response) {
			if(response.userName != undefined){
				$('#userName').html(response.userName);
			}
			
			if(response.code != null) {
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
			}
			
			if(response.commuteList != undefined && response.commuteList.length > 0){
				let tbody = $('#commuteTbody');
				tbody.empty();
				$.each(response.commuteList, function(index, item) {
					let row = '<tr>' +
						'<td>' + (index + 1) + '</td>' +
						'<td>' + item.commute_type + '</td>' +
						'<td>' + item.user_name + '</td>' +
						'<td>' + item.checked_time + '</td>' +
						'</tr>';
					tbody.append(row);
				});
			}
			
			if(response.noticeList != undefined && response.noticeList.length > 0) {
				response.noticeList.forEach(function(notice) {
					let tempDiv = $('<div>').html(notice.content);
					let plainTextContent = tempDiv.text();
					let noticeHTML = `
						<div class="d-flex align-items-start border-left-line pb-3">
							<div>
								<a href="/viewNotice?id=${notice.id}" class="btn btn-danger btn-circle mb-2 btn-item"><i class="fas fa-bullhorn"></i></a>
							</div>
							<div class="ms-3 mt-2">
								<h5 class="text-dark font-weight-medium mb-2">${notice.title}</h5>
								<p class="font-14 mb-2 text-muted">${plainTextContent}</p>
								<span class="font-weight-light font-14 text-muted">${new Date(notice.created_dt).toLocaleString()}</span>
							</div>
						</div>`;
					$('#notices').append(noticeHTML);
				});
			}
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
}