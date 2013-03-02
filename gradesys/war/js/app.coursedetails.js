/***** Course Details *****/
app.model.courseDetails = {};

(function() {
   
   var name;
   
   var id;
   
   
   var courseDetails;
   
   app.model.courseDetails.setId = function(data) {
      id = data
   };
   
   app.model.courseDetails.getName = function() {
      return name;
   };

   app.model.courseDetails.setName = function(data) {
      name = data;
   };

   app.model.courseDetails.getId = function() {
      return id;
   };

   app.model.courseDetails.setCourseDetails = function(data) {
      courseDetails = data;
   };

   app.model.courseDetails.getCourseDetails = function() {
      return courseDetails;
   };
})();

app.view.courseDetails = {};

(function() {
   
   app.view.courseDetails.render = function() {
      
      $('#copcontent').empty();
      $("#copcontent > p").remove();
      var courseDetails =  app.model.courseDetails.getCourseDetails();
      
      if(courseDetails === null || courseDetails.courseName === null)
         return;
      
      
      var $p1 = $('<p></p>');
      
      var $l = $('<label for="courseName" class="ui-bar ui-bar-b"></label>');
      $l.append('Course Name: ' + courseDetails.courseName);
      
      //var coursename = "Course Name : "  + courseDetails.courseName;
      $p1.append($l);      
      $('#copcontent').append($p1);      
      $('#courseDetails').trigger('create');
   };
      
})();

app.controller.courseDetails = {};

(function(jQuery, mcourseList, vcourseDetails) {
   
   app.controller.courseDetails.listCourse = function() {
      var id = app.model.courseDetails.getId();
      $.ajax({
            type : 'POST',
            url : "/instructor/controller",
            data : {
               id : id,
               op : 'listcourse'
            },
            dataType : "json"
      }).done(function(data) {
         if (data.err) {
            console.log(data.err);
         } else if(data.redirectUrl) {
          //app.view.transfer("../index.html");
          window.location.href = data.redirectUrl;
         }          
         else {
            app.model.courseDetails.setCourseDetails(data);
            vcourseDetails.render();
         }
      }).fail(function(jqXHR, textStatus) {
            consol.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
   };

   app.controller.courseDetails.saveCourse = function() {
      var newCourseName = $('#courseName').val();
      var oldCourseName = app.model.courseDetails.getName();
      $.ajax({
            type : 'POST',
            url : "/instructor/controller",
            data : {
               oldCourseName : oldCourseName,
               newCourseName : newCourseName,
               op : 'coursesave'
            },
            dataType : "json"
      }).done(function(data) {
         if (data.err) {
            alert(data.err);
         } else if(data.redirectUrl) {
          //app.view.transfer("../index.html");
          window.location.href = data.redirectUrl;
         } 
         else {
            mcourseList.update(data);
            app.model.courseDetails.setCourseName(newCourseName);
            vcourseDetails.render(newCourseName);
         }
      }).fail(function(jqXHR, textStatus) {
            consol.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
   };
   
  
})(jQuery, app.model.courseList, app.view.courseDetails);