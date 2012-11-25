

function createPageOne(div) {
	var $pageDive = $('<div id="pageOne" data-role="page"></div>');
	var $contentdiv = $('<div data-role="content"></div>');
	$contentDiv.html("hi");
	$pageDiv.append($contentDiv);
	$(div).append($pageDiv);
	
}