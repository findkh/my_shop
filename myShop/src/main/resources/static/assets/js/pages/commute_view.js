$(document).ready(function() {
	
	let currentDate = new Date();
	
	// 년도와 월 가져오기
	let currentYear = currentDate.getFullYear();
	let currentMonth = currentDate.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줍니다.
	
	// 년도와 월을 문자열로 변환
	let yearStr = currentYear.toString();
	let monthStr = currentMonth < 10 ? '0' + currentMonth : currentMonth.toString();
	
	$('#currentYear').val(yearStr);
	$('#currentMonth').val(monthStr);
	
	const employeeGrid = new tui.Grid({
		el: document.getElementById('commuteGrid'),
		data: [],
		scrollX: false,
		scrollY: true,
		rowHeaders: ['rowNum'],
		columns: [
			{ name: 'id', hidden: true},
			{ header: '구분', name: 'commute_type'},
			{ header: '시간', name: 'checked_time'},
		]
	});

	$.ajax({
		url: '/getUserCommuteList',
		type: 'GET',
		data: {
			year: yearStr,
			month: monthStr
		},
		success: function(data) {
			employeeGrid.resetData(data);
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});

//	$('#searchBtn').click(function(){
//		if($('#searchInput').val() == ''){
//			alert('이름 또는 전화번호를 입력해주세요.');
//			return false;
//		}
//		
//		$.ajax({
//			type: 'GET',
//			url: '/findEmployee',
//			data: {
//				searchKeyword: $('#searchInput').val()
//			},
//			success: function(result) {
//				if(result.length > 0){
//					employeeGrid.resetData(result);
//				}
//			},
//			error: function(xhr, status, error) {
//				console.error('요청 실패:', error);
//			}
//		});
//	});
//	
//	$('#addBtn').click(function(){
//		window.location.href = '/addEmployeeInfo';
//	})
});

