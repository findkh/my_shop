$(document).ready(function() {
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
	
	let currentDate = new Date();
	let currentYear = currentDate.getFullYear();
	let currentMonth = currentDate.getMonth() + 1; // 월은 0부터 시작하므로 1을 더해줍니다.
	let yearStr = currentYear.toString();
	let monthStr = currentMonth < 10 ? '0' + currentMonth : currentMonth.toString();
	
	$('#currentYear').val(yearStr);
	$('#currentMonth').val(monthStr);

	getUserCommuteList(yearStr, monthStr);
	
	$('#currentMonth').on('change', function(){
		getUserCommuteList(yearStr, $(this).val());
	})
	
	function getUserCommuteList(yearStr, monthStr){
		if(monthStr == ''){
			alert('조회할 월을 선택해주세요');
			return false;
		}
		
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
	}
});