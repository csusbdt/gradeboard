/*$.ajaxSetup({
    complete: function (request, textStatus) {
        // header inserted manually on the server.
        // you should block the automatic redirect headers 
        // inserted by the server.
        var location = request.getResponseHeader("Location");
        if(location) window.location = location; 
    }
});*/

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


/*** App class ***/

(function() {   
   app.logout = function() {
      $.ajax({
         type : 'GET',
         url : "/instructor/logout",
         data : {
            op : 'logout'
         },
         dataType : "json"
      }).done(function(data) {
         if (data.err) {
            console.log(data.err);
         } else if(data.redirectUrl) {
            //app.view.transfer("../home.html");
            window.location.href = data.redirectUrl + "../index.html";
         }
      }).fail(function(jqXHR, textStatus) {
            //console.log(textStatus);
            location.href = jqXHR.getResponseHeader("Location");
            //$.mobile.hidePageLoadingMsg();
      });
  }
  
  app.login = function() {      
     $.ajax({
           type : 'GET',
           url : "/instructor/index.html",
           data : {
              op : 'login',
              page : 'index.html'
           },
           dataType : "json"
     }).done(function(data) {
        if (data.err) {
           console.log(data.err);
        } else if(data.redirectUrl) {
         //app.view.transfer("../index.html");
         
         window.location.replace(data.redirectUrl);
      }
     }).fail(function(jqXHR, textStatus) {
           //console.log(textStatus);
           location.href = jqXHR.getResponseHeader("Location");
           //$.mobile.hidePageLoadingMsg();
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
      var count = courses.courses.length;
      $.mobile.hidePageLoadingMsg();
      $.mobile.showPageLoadingMsg("a", "Loading Course List....");
      $.each(courses.courses, function(i, course) {
            var $p = $('<p></p>');
            var query = "CourseOperations.html"; //?courseName=" + course;
            var onclick = "app.model.courseOperations.setName('" + course.name + "');app.model.courseOperations.setId(" + course.id + ");app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
            $('#main').append($p);
            $a.html(course.name);
            $p.append($a);
            if(i == count - 1) {
               $('#courseList').trigger('create');
               $.mobile.hidePageLoadingMsg();
            }
      });
      
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
            op : 'listCourses',
            page : 'index.html'
         },
         dataType : "json"
      }).done(function(data) {
         if (data.err) {
            console.log(data.err);
         } else if(data.redirectUrl) {
          //app.view.transfer("../index.html");
          window.location.href.replace(data.redirectUrl);
         } 
         else {
            mcourseList.update(data);
            vcourseList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      //$('#courseList').die().live('pagechange', vcourseList.render);
     
      //};
   };
   
  
})(jQuery, app.model.courseList, app.view.courseList);


/***** Course Operations *****/
app.model.courseOperations = {};

(function() {
   
   var name;
   
   var id;
   
   
   var courseDetails;
   
   app.model.courseOperations.setId = function(data) {
      id = data
   };
   
   app.model.courseOperations.getName = function() {
      return name;
   };

   app.model.courseOperations.setName = function(data) {
      name = data;
   };

   app.model.courseOperations.getId = function() {
      return id;
   };

   app.model.courseOperations.setCourseDetails = function(data) {
      courseDetails = data;
   };

   app.model.courseOperations.getCourseDetails = function() {
      return courseDetails;
   };
})();

app.view.courseOperations = {};

(function() {
   
   app.view.courseOperations.render = function(coursename) {
      
      $('#copContent').empty();
      $("#copContent > p").remove();
      var courseDetails =  app.model.courseOperations.getCourseDetails();
      
      if(courseDetails === null || courseDetails.courseName === null)
         return;
      
      
      var $p1 = $('<p></p>');
      var coursename = "Course Name : "  + courseDetails.courseName;
      $p1.append(coursename);      
      $('#copContent').append($p1);
      
      
      
      $('#copContent').trigger('create');
   };
      
})();

app.controller.courseOperations = {};

(function(jQuery, mcourseList, vcourseOperations) {
   
   app.controller.courseOperations.listCourse = function() {
      var id = app.model.courseOperations.getId();
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
            app.model.courseOperations.setCourseDetails(data);
            vcourseOperations.render(newCourseName);
         }
      }).fail(function(jqXHR, textStatus) {
            consol.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
   };

   app.controller.courseOperations.saveCourse = function() {
      var newCourseName = $('#courseName').val();
      var oldCourseName = app.model.courseOperations.getName();
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
            app.model.courseOperations.setCourseName(newCourseName);
            vcourseOperations.render(newCourseName);
         }
      }).fail(function(jqXHR, textStatus) {
            consol.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
   };
   
  
})(jQuery, app.model.courseList, app.view.courseOperations);

/*** Course Edit ***/

app.model.courseEdit = {};

(function() {
   
   var courseName;
   
   app.model.courseEdit.update = function(data) {
      courseName = data;
    };
    
    app.model.courseEdit.get = function() {
       return courseName;
    };
})();

app.view.courseEdit = {};

(function() {
   var courses;
   
   app.view.courseEdit.render = function(coursename) {
      $('#title').empty();
      $('#title').append(coursename);
      $('#courseName').val(coursename);
      $('#header').trigger('create');
   };
      
})();

app.controller.courseEdit = {};


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

