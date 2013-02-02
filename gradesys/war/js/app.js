var app = {
   controller : {},
   view : {},
   model : {},
   util : {}
};

/*** App view for all the classes ***/

(function() {
   var courses;
   
   app.view.transfer = function(toPage) {      
      $.mobile.changePage(toPage,
      {
         allowSamePageTransition : true,
         transition : 'none',
         reloadPage : false
      });
   }   
})();

/*** Course List ****/
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
            var query = "CourseOperations.html"; //?courseName=" + course;
            var onclick = "app.model.courseOperations.update('" + course + "');app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
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


/***** Course Operations *****/
app.model.courseOperations = {};

(function() {
   
   var courseName;
   
   app.model.courseOperations.update = function(data) {
      courseName = data;
    };
    
    app.model.courseOperations.get = function() {
       return courseName;
    };
})();

app.view.courseOperations = {};

(function() {
   var courses;
   
   app.view.courseOperations.render = function(coursename) {
      $('#title').empty();
      $('#title').append(coursename);
      $('#courseName').val(coursename);
      $('#header').trigger('create');
   };
      
})();

app.controller.courseOperations = {};

(function(jQuery, mcourseList, vcourseOperations) {

   app.controller.courseOperations.saveCourse = function() {
      var newCourseName = $('#courseName').val();
      var oldCourseName = app.model.courseOperations.get();
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
         } else {
            mcourseList.update(data);
            app.model.courseOperations.update(newCourseName);
            vcourseOperations.render(newCourseName);
         }
      }).fail(function(jqXHR, textStatus) {
            consol.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
   };
   
  
})(jQuery, app.model.courseList, app.view.courseOperations);

/*** Course View ***/

app.model.courseView = {};

(function() {
   
   var courseName;
   
   app.model.courseView.update = function(data) {
      courseName = data;
    };
    
    app.model.courseView.get = function() {
       return courseName;
    };
})();

app.view.courseView = {};

(function() {
   var courses;
   
   app.view.courseView.render = function(coursename) {
      $('#title').empty();
      $('#title').append(coursename);
      $('#courseName').val(coursename);
      $('#header').trigger('create');
   };
      
})();

app.controller.courseView = {};


/****** Utility ******/
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
     $('#popupErrorMsg').html(error);         
     $('#popupError').popup('open');
   }
})();

