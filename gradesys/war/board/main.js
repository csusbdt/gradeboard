
function getURLParameter(name) {
  return decodeURIComponent((new RegExp('[?|&]' + name + '=' + '([^&;]+?)(&|#|;|$)').exec(location.search)||[,""])[1].replace(/\+/g, '%20'))||null;
}

$(document).ready(function() {
	var courseId = getURLParameter('courseId');
	var studentId = getURLParameter('studentId');
	if (courseId === null) {
		$('body').append('<div>courseId missing</div>');
	}
	else {
		var url = '/student/controller';
		$.ajax({
			url: url,
			dataType: 'json',
			type: 'POST',
			data: { courseId: courseId, random: new Date().getTime() },
			success: processSuccess,
			error: processError
		});
	}
});

function processError(jqXHR, ajaxSettings, thrownError) {
	if (jqXHR.status == 404) {
		$('body').html('<div>Course not found.</div>');
	} else {
		processUnexpectedError(jqXHR, ajaxSettings, thrownError);
	}
}

function processUnexpectedError(jqXHR, ajaxSettings, thrownError) {
	$('body').html('<div>Error</div>');
	$('body').append($('<div/>').html('url: ' + ajaxSettings.url));
	$('body').append($('<div/>').html('jqXHR.status: ' + jqXHR.status));
	$('body').append($('<div/>').html('thrownError: ' + thrownError));
}

function processSuccess(data) {
	var i, j, $row, $cell, $div;
	
	var studentId = getURLParameter('studentId');
	if (studentId === null) {
		if (localStorage) studentId = localStorage.getItem('studentId');
	}

	$('title').html(data.title);
	$('body').append('<heading>' + data.title + '</heading>');
	
	var $select = $('<select></select>');
	$select.append('<option></option>');
	$select.change(function() {
		var studentId = $(this).val();
		$('tr').removeClass('selected');
		$('.studentId' + studentId).addClass('selected');
		if (localStorage) localStorage.setItem('studentId', studentId);
	});
	
	var $table = $('<table></table>');
	$('body').append($table);
	$row = $('<tr></tr>');
	$table.append($row);
	$row.append($('<th rowspan="3">student<br></th>').append($select));
	$row.append('<th rowspan="3">rank</th>');
	$row.append('<th colspan="' + data.objectives.length +'">Gradables</th>');
	$row.append('<th rowspan="3">points<br>earned</th>');
	$row.append('<th rowspan="3">points<br>attempted</th>');
	$row.append('<th rowspan="3">current<br>percent</th>');
	$row.append('<th rowspan="3">current<br>grade</th>');
	
	$row = $('<tr></tr>');
	$table.append($row);
    for (j = 0; j < data.objectives.length; ++j) {
        $row.append('<th style="border-bottom-style: none">' + data.objectives[j] + '</th>');
    }
    
	$row = $('<tr></tr>');
	$table.append($row);
    for (j = 0; j < data.points.length; ++j) {
		$row.append('<th style="border-top-style: none; font-style:italic">' + data.points[j] + '</th>');
    }
    
    // Fill table rows.
    data.students.sort(function(a, b) { return b.total - a.total });    
    for (i = 0; i < data.students.length; ++i) { 
        var student = data.students[i];
		$row = $('<tr></tr>');
    	if (studentId !== null && studentId === student.id) {
    		$row.addClass('selected');
    	}
    	$row.addClass('studentId' + student.id);
    	$table.append($row);
        $row.append('<th>' + student.id + '</th>');
        $row.append('<td>' + student.rank + '</td>');
        for (j = 0; j < student.scores.length; ++j) {
        	$cell = $('<td></td>');
        	$cell.html(student.scores[j]);
        	$cell.data('objective', data.objectives[j]);
        	$cell.click(function() {
        		var $cell = $(this);
        		var score = $cell.html(); 
        		$cell.html($cell.data('objective'));
        		setTimeout(function() { 
        			$cell.html(score); 
        		}, 1500);
        	});
        	$row.append($cell);
        }
        $row.append('<td>' + student.total + '</td>');
        $row.append('<td>' + student.attempted + '</td>');
        $row.append('<td>' + student.percent + '</td>');
        $row.append('<td>' + student.grade + '</td>');
    }

    // Populate student id selection element.
    data.students.sort(function(a, b) { return a.id.localeCompare(b.id); });
    for (i = 0; i < data.students.length; ++i) { 
        var student = data.students[i];
        if (studentId !== null && studentId === student.id) {
        	$select.append('<option  selected="selected">' + student.id + '</option>');
        	if (localStorage) localStorage.setItem('studentId', studentId);
        } else {
        	$select.append('<option>' + student.id + '</option>');
        }
    }
}
