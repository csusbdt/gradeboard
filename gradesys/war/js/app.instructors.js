/*** Instructors List ****/
app.model.instructorList = {};

(function() {
   
   var instructors;
   
   app.model.instructorList.update = function(data) {
      instructors = data;
    };
    
    app.model.instructorList.get = function() {
       return instructors;
    };
})();

app.view.instructorList = {};

(function(minstructorList) {

   var instructors;

   app.view.instructorList.render = function() {

      //render : new function() {
      if(instructors === null && minstructorList.get() === null)
         return;
           
      if (instructors === minstructorList.get())         
         return;
      
      instructors = minstructorList.get();
      $("#instructorList > p").remove();
      var count = instructors.instructors.length;
      $.mobile.hidePageLoadingMsg();
      $.mobile.showPageLoadingMsg("a", "Loading Instructor List....");
      $.each(instructors.instructors, function(i, instructor) {
            var $p = $('<p></p>');
            var query = "InstructorDetails.html"; //?instructorName=" + instructor;
            var onclick = "app.model.instructorDetails.setName('" + instructor.name + "');app.model.instructorDetails.setEmail('" + instructor.email + "');app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" style="text-align:left" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
            $('#instructorList').append($p);
            $a.html(instructor.name);
            $p.append($a);
            if(i == count - 1) {
               $('#instructorList').trigger('create');
               $.mobile.hidePageLoadingMsg();
            }
      });
      
   };
   
   
})(app.model.instructorList);

app.controller.instructorList = {};

(function(jQuery, minstructorList, vinstructorList) {

   app.controller.instructorList.init = function() {

      // render : new function() {
      //minstructorList.update(vinstructorList.render);      
      $.ajax({
            type : 'GET',
            url : "/instructor/controller",
            data : {
            op : 'listinstructor',
            courseId : app.model.courseEdit.getId(),
            page : 'Instructors.html'
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
            minstructorList.update(data);
            vinstructorList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      //$('#instructorList').die().live('pagechange', vinstructorList.render);
     
      //};
   };
   
  
})(jQuery, app.model.instructorList, app.view.instructorList);


/*** Instructor Details ***/
app.model.instructorDetails = {};

(function() {
   
   var name;
   
   var email;
   
   
   var instructorDetails;
   
   app.model.instructorDetails.setEmail = function(data) {
      email = data
   };
   
   app.model.instructorDetails.getName = function() {
      return name;
   };

   app.model.instructorDetails.setName = function(data) {
      name = data;
   };

   app.model.instructorDetails.getEmail = function() {
      return email;
   };

   app.model.instructorDetails.setInstructorDetails = function(data) {
      instructorDetails = data;
   };

   app.model.instructorDetails.getInstructorDetails = function() {
      return instructorDetails;
   };
})();

app.view.instructorDetails = {};

(function() {
   
   app.view.instructorDetails.render = function() {
      
      $('#inscontent').empty();
      $("#inscontent > p").remove();
      var name =  app.model.instructorDetails.getName();
      var email =  app.model.instructorDetails.getEmail();
      if(name === null)
         return;
     
      
      var $p1 = $('<p></p>');
      
      var $l = $('<label for="instructorName" class="ui-bar ui-bar-b"></label>');
      $l.append('Instructor Name: ' + name);
      
      $p1.append($l);      
      $('#inscontent').append($p1); 
      
      var $p2 = $('<p></p>');
            
      var $l1 = $('<label for="instructorEmail" class="ui-bar ui-bar-b"></label>');
      $l1.append('Instructor Email: ' + email);

      $p2.append($l1);      
      $('#inscontent').append($p2); 
      
      $('#instructorsDetails').trigger('create');
   };
      
})();