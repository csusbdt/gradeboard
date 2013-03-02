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
            var query = "CourseDetails.html"; //?courseName=" + course;
            var onclick = "app.model.courseDetails.setName('" + course.name + "');app.model.courseDetails.setId(" + course.id + ");app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" style="text-align:left" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
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

