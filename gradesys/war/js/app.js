var app = {
   controller : {},
   view : {},
   model : {},
   util : {}
};

app.model.courseList = {};

(function() {
   
   var courses;
   
   app.model.courseList.update = function(data) {
      courses = data;
    };
    
    app.model.courseList.get = function() {
       return courses;
    };
})();

app.view.courseList = {};

(function(mcourseList) {

   var courses;

   app.view.courseList.render = function() {

      //render : new function() {
      if(courses === null && mcourseList.get() === null)
         return;
           
      if (courses === mcourseList.get())         
         return;
      
      courses = mcourseList.get();
      $("#main > p").remove();
      $.each(courses.courses, function(i, course) {
            var $p = $('<p></p>');
            var query = "CourseOperations.html?courseName=" + course;
            var $a = $('<a data-role="button" data-transition="slide" href="' + query + '"></a>');            
            $('#main').append($p);
            $a.html(course);
            $p.append($a);
         });
      $('#main').trigger('create');
   };
   
   
})(app.model.courseList);

app.controller.courseList = {};

(function(jQuery, mcourseList, vcourseList) {

   app.controller.courseList.init = function() {

      // render : new function() {
      //mcourseList.update(vcourseList.render);      
      $.ajax({
            type : 'GET',
            url : "/instructor/controller",
            data : {
            op : 'listCourses'
         },
         dataType : "json"
      }).done(function(data) {
         if (data.err) {
            alert(data.err);
         } else {
            mcourseList.update(data);
            vcourseList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            consol.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      $('#courseList').die().live('pagechange', vcourseList.render);
     
      //};
   };
   
  
})(jQuery, app.model.courseList, app.view.courseList);

(function() {

   app.util.getUrlVars = function() {
      var   vars = [], hash;
      var   href = window.location.href;
      var   queryUrl =href.slice(href.lastIndexOf( '?' ) + 1);
      var   hashes = queryUrl.split( '&' );
      for ( var   i = 0; i < hashes.length; i++) {
        hash = hashes[i].split( '=' );
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
      }
      return   vars;
   }
})();

(function() {
   app.view.displayError = function(error) {
     var $errorPopup = $('#errorPopup');
     if($errorPopup.length === 0) {
        $errorPopup = $('div id="popupError" data-role="popup"><a href="#" data-rel="back" data-role="button" data-theme="a" data-icon="delete" data-iconpos="notext" class="ui-btn-right">Close</a><div id="popupErrorMsg">Test</div>');
        $('div[data-role="page"]').prepend($errorPopup);
        
        $('body').trigger('create');
     }
     console.log($('#popupErrorMsg'));
     //console.log(error);
     
     $errorPopup.popup('open');
     
   }
})();

