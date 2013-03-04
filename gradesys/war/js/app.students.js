/*** Students List ****/
app.model.studentList = {};

(function() {
   
   var students;
   
   app.model.studentList.update = function(data) {
      students = data;
    };
    
    app.model.studentList.get = function() {
       return students;
    };
})();

app.view.studentList = {};

(function(mstudentList) {

   var students;

   app.view.studentList.render = function() {

      //render : new function() {
      if(students === null && mstudentList.get() === null)
         return;
           
      if (students === mstudentList.get())         
         return;
      
      students = mstudentList.get();
      $("#studentList > p").remove();
      var count = students.students.length;
      $.mobile.hidePageLoadingMsg();
      $.mobile.showPageLoadingMsg("a", "Loading Student List....");
      $.each(students.students, function(i, student) {
            var $p = $('<p></p>');
            var query = "StudentEdit.html"; //?studentName=" + student;
            var onclick = "app.model.studentEdit.setName('" + student.name + "');app.model.studentEdit.setEmail('" + student.email + "');app.model.studentEdit.setId('" + student.id + "');app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" style="text-align:left" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
            $('#studentList').append($p);
            $a.html(student.name);
            $p.append($a);
            if(i == count - 1) {
               $('#studentList').trigger('create');
               $.mobile.hidePageLoadingMsg();
            }
      });
      
   };
   
   
})(app.model.studentList);

app.controller.studentList = {};

(function(jQuery, mstudentList, vstudentList) {

   app.controller.studentList.init = function() {

      // render : new function() {
      //mstudentList.update(vstudentList.render);      
      $.ajax({
            type : 'GET',
            url : "/student/controller",
            data : {
            op : 'liststudent',
            courseId : app.model.courseEdit.getId(),
            page : 'Students.html'
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
            mstudentList.update(data);
            vstudentList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      //$('#studentList').die().live('pagechange', vstudentList.render);
     
      //};
   };
   
  
})(jQuery, app.model.studentList, app.view.studentList);


/*** Student Edit ***/
app.model.studentEdit = {};

(function() {
   
   var name;
   
   var email;
   
   var id;
   
   var studentEdit;
   
   app.model.studentEdit.setEmail = function(data) {
      email = data
   };
   
   app.model.studentEdit.getName = function() {
      return name;
   };

   app.model.studentEdit.setName = function(data) {
      name = data;
   };

   app.model.studentEdit.getEmail = function() {
      return email;
   };
   
   app.model.studentEdit.setId = function(data) {
      id = data;
   };
   
   app.model.studentEdit.getId = function() {
      return id;
   };

   app.model.studentEdit.setStudentEdit = function(data) {
      studentEdit = data;
   };

   app.model.studentEdit.getStudentEdit = function() {
      return studentEdit;
   };
})();

app.view.studentEdit = {};

(function() {
   
   app.view.studentEdit.render = function() {
      
      $('#studentEditContent').empty();
      $("#studentEditContent > p").remove();
      var name =  app.model.studentEdit.getName();
      var email =  app.model.studentEdit.getEmail();
      if(name === null)
         return;
     
      
      $('#studentName').val(name);
      $('#studentEmail').val(email);
   };
      
})();