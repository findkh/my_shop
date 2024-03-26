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
			console.log(rowData)
			        // 클릭된 행의 ID 가져오기
			const employeeId = rowData.id;

			// viewEmployeeInfo 페이지로 이동하고 URL에 직원의 ID를 추가
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
	})
});

