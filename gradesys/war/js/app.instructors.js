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
            var onclick = "app.model.instructorDetails.setName('" + instructor.name + "');app.model.instructorDetails.setId(" + instructor.id + ");app.view.transfer('" + query + "')";
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
