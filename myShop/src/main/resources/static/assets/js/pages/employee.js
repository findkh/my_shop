$(document).ready(function() {
	const employeeGrid = new tui.Grid({
		el: document.getElementById('employeeGrid'),
		data: [],
		scrollX: false,
		scrollY: true,
		rowHeaders: ['rowNum'],
		columns: [
			{ name: 'id', hidden: true},
			{ header: '이름', name: 'user_name', className: 'column_click'},
			{ header: '재직상태', name: 'status_nm', className: 'column_click' },
		]
	});

	employeeGrid.on('click', (ev) => {
		const { rowKey, columnName } = ev;
		if(columnName == 'user_name' || columnName == 'status_nm'){
			const rowData = employeeGrid.getRow(rowKey);
			const employeeId = rowData.id;
			window.location.href = '/viewEmployeeInfo?id=' + employeeId;
		}
	});

	$.ajax({
		url: '/getEmployeeList',
		type: 'GET',
		success: function(data) {
			employeeGrid.resetData(data);
		},
		error: function(xhr, status, error) {
			console.error(error);
		}
	});

	$('#searchBtn').click(function(){
		if($('#searchInput').val() == ''){
			alert('이름 또는 전화번호를 입력해주세요.');
			return false;
		}
		
		$.ajax({
			type: 'GET',
			url: '/findEmployee',
			data: {
				searchKeyword: $('#searchInput').val()
			},
			success: function(result) {
				if(result.length > 0){
					employeeGrid.resetData(result);
				}
			},
			error: function(xhr, status, error) {
				console.error('요청 실패:', error);
			}
		});
	});
	
	$('#addBtn').click(function(){
		window.location.href = '/addEmployeeInfo';
	})
});

