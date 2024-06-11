$(document).ready(function() {
	let urlParams = new URLSearchParams(window.location.search);
	let pageNumber = urlParams.get('pageNumber') || 1; // 쿼리스트링이 없으면 1로 설정
	getNoticeList(pageNumber);
});

function getNoticeList(pageNumber) {
	$.ajax({
		url: '/getNoticeList',
		type: 'GET',
		data: { pageNumber: pageNumber },
		success: function(response) {
			// 공지사항 목록을 화면에 표시
			displayNotices(response.noticeList);
			// 페이지네이션을 동적으로 생성
			createPagination(response.totalPages, parseInt(pageNumber));
		},
		error: function(xhr, status, error) {
			console.error(xhr.responseText);
		}
	});
}

function displayNotices(notices) {
	let noticeTableBody = $('#noticeTableBody');
	noticeTableBody.empty(); // 기존 내용을 비움
	
	notices.forEach(function(notice, index) {
		let noticeRow = $('<tr></tr>');
		noticeRow.append('<td class="border-top-0 text-dark px-2 py-4 font-14 font-weight-medium text-center">' + notice.id + '</td>');
		noticeRow.append('<td class="border-top-0 text-dark px-2 py-4 font-14 font-weight-medium">' + notice.title + '</td>');
		noticeRow.append('<td class="border-top-0 text-dark px-2 py-4 font-14 font-weight-medium text-center">' + notice.name + '</td>');
		noticeRow.append('<td class="border-top-0 text-dark px-2 py-4 font-14 font-weight-medium text-center">' + formatDate(notice.created_dt) + '</td>');
		noticeTableBody.append(noticeRow);
	});
}

function createPagination(totalPages, currentPage) {
	let paginationContainer = $('.pagination');
	paginationContainer.empty(); // 기존 페이지네이션 내용 비우기
	
	// 이전 버튼
	let prevClass = currentPage === 1 ? 'disabled' : '';
	paginationContainer.append('<li class="page-item ' + prevClass + '"><a class="page-link" href="#" data-page="' + (currentPage - 1) + '">Previous</a></li>');
	
	// 페이지 번호 버튼
	for (let i = 1; i <= totalPages; i++) {
		let activeClass = i === currentPage ? 'active' : '';
		paginationContainer.append('<li class="page-item ' + activeClass + '"><a class="page-link" href="#" data-page="' + i + '">' + i + '</a></li>');
	}

	// 다음 버튼
	let nextClass = currentPage === totalPages ? 'disabled' : '';
	paginationContainer.append('<li class="page-item ' + nextClass + '"><a class="page-link" href="#" data-page="' + (currentPage + 1) + '">Next</a></li>');
	
	// 페이지 버튼 클릭 이벤트 바인딩
	$('.page-link').click(function(event) {
		event.preventDefault();
		let page = $(this).data('page');
		if (page > 0 && page <= totalPages) {
			updateURLParameter('pageNumber', page);
			getNoticeList(page);
		}
	});
}

// URL의 쿼리스트링을 업데이트하는 함수
function updateURLParameter(param, value) {
	let currentUrl = window.location.href;
	let url = new URL(currentUrl);
	let params = new URLSearchParams(url.search);
	
	params.set(param, value);
	url.search = params.toString();
	
	window.history.replaceState({}, '', url.toString());
}

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