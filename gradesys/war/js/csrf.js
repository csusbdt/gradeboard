// https://developer.mozilla.org/en-US/docs/DOM/document.cookie

// page loads:
// app.csrfToken = docCookies.getItem('csrf');

/*

Example sending csrf token to server:

app.save_number = function() {
  $.ajax({
    url: '/save-number',
    type: 'POST',
    data: { 'number': app.personalNumber, 'csrf': app.csrfToken }
  })
  .done(function() { alert('personal number saved'); })
  .fail(function(jqxhr, textMessage) {
    $('#error').html(textMessage || 'An error occurred.');
   });
}

*/
