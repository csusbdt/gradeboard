
/*** Course Edit ***/

app.model.courseEdit = {};

(function() {
   
   var courseName;
   var courseId;
   
   app.model.courseEdit.update = function(cn, cId) {
      courseName = cn;
      courseId = cId;
    };
    
    app.model.courseEdit.getName = function() {
       return courseName;
    };
    
    app.model.courseEdit.getId = function() {
       return courseId;
    };
    
})();

app.view.courseEdit = {};

(function() {
   var courses;
   
   /*** Course Edit Main page render ****/
   app.view.courseEdit.render = function(coursename) {
      var $l = $('<label for="courseName" class="ui-bar ui-bar-b"></label>');
      $l.append(coursename);
      $('#legend').append($l);
      $('#editCourse').trigger('create');
   };
      
})();


app.view.courseNameEdit = {};

(function() {
   var courses;
   
   /*** Edit course name page render ****/
   app.view.courseNameEdit.render = function(coursename) {
     $('#courseName').val(coursename);
   };
      
})();


app.controller.courseEdit = {};

/******** Course Name Edit *******************/

