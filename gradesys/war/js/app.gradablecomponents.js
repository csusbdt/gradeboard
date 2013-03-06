/*** GradableComponents List ****/
app.model.gcList = {};

(function() {
   
   var gcs;
   
   app.model.gcList.update = function(data) {
      gcs = data;
    };
    
    app.model.gcList.get = function() {
       return gcs;
    };
})();

app.view.gcList = {};

(function(mgcList) {

   var gcs;

   app.view.gcList.render = function() {

      //render : new function() {
      if(gcs === null && mgcList.get() === null)
         return;
           
      if (gcs === mgcList.get())         
         return;
      
      gcs = mgcList.get();
      $("#gcList > p").remove();
      var count = gcs.gcs.length;
      $.mobile.hidePageLoadingMsg();
      $.mobile.showPageLoadingMsg("a", "Loading GradableComponent List....");
      $.each(gcs.gcs, function(i, gc) {
            var $p = $('<p></p>');
            var query = "GradableComponentEdit.html"; //?gcName=" + gc;
            var onclick = "app.model.gcEdit.setName('" + gc.name + "');app.model.gcEdit.setPoints('" + gc.points + "');app.model.gcEdit.setId('" + gc.id + "');app.model.gcEdit.setDeadline('" + gc.deadline + "');app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" style="text-align:left" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
            $('#gcList').append($p);
            $a.html(gc.name);
            $p.append($a);
            if(i == count - 1) {
               $('#gcList').trigger('create');
               $.mobile.hidePageLoadingMsg();
            }
      });
      
   };
   
   
})(app.model.gcList);

app.controller.gcList = {};

(function(jQuery, mgcList, vgcList) {

   app.controller.gcList.init = function() {

      // render : new function() {
      //mgcList.update(vgcList.render);      
      $.ajax({
            type : 'GET',
            url : "/grade/controller",
            data : {
            op : 'listgcs',
            courseId : app.model.courseEdit.getId(),
            page : 'GradableComponents.html'
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
            mgcList.update(data);
            vgcList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      //$('#gcList').die().live('pagechange', vgcList.render);
     
      //};
   };
   
  
})(jQuery, app.model.gcList, app.view.gcList);


/*** GradableComponent Edit ***/
app.model.gcEdit = {};

(function() {
   
   var name;
   
   var points;
   
   var id;
   
   var deadline;
   
   var gcEdit;
   
   app.model.gcEdit.setPoints = function(data) {
      points = data
   };
   
   app.model.gcEdit.getPoints = function() {
      return points;
   };
   
   app.model.gcEdit.getName = function() {
      return name;
   };

   app.model.gcEdit.setName = function(data) {
      name = data;
   };

   app.model.gcEdit.setId = function(data) {
      id = data;
   };
   
   app.model.gcEdit.getId = function() {
      return id;
   };
   
   app.model.gcEdit.setDeadline = function(data) {
      deadline = data;
   };

   app.model.gcEdit.getDeadline = function() {
      return deadline;
   };


   app.model.gcEdit.setGCEdit = function(data) {
      gcEdit = data;
   };

   app.model.gcEdit.getGCEdit = function() {
      return gcEdit;
   };
})();

app.view.gcEdit = {};

(function() {
   
   app.view.gcEdit.render = function() {
      
      //$('#gcEditContent').empty();
      var name =  app.model.gcEdit.getName();
      var points =  app.model.gcEdit.getPoints();
      var deadline =  app.model.gcEdit.getDeadline();
      
      if(name === null)
         return;
     
      
      $('#gcName').val(name);
      $('#gcPoints').val(points);
      $('#gcDeadline').val(deadline);
   };
      
})();

/***********Grades ***************/
app.model.gradesList = {};

(function() {
   
   var grades;
   
   app.model.gradesList.update = function(data) {
      grades = data;
    };
    
    app.model.gradesList.get = function() {
       return grades;
    };
})();

app.view.gradesList = {};

(function(gradesList) {

   var grades;

   app.view.gradesList.render = function() {

      //render : new function() {
      if(grades === null && gradesList.get() === null)
         return;
           
      if (grades === gradesList.get())         
         return;
      
      grades = gradesList.get();
      $("#gradesList > p").remove();
      var count = grades.grades.length;
      $.mobile.hidePageLoadingMsg();
      $.mobile.showPageLoadingMsg("a", "Loading Grades List....");
      $.each(grades.grades, function(i, grade) {
            var $p = $('<p></p>');
            var query = "GradeEdit.html"; //?gcName=" + gc;
            var onclick = "app.model.gradesEdit.setName('" + grade.name + "');app.model.gradesEdit.setId('" + grade.id + "');app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" style="text-align:left" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
            $('#gradesList').append($p);
            $a.html(grade.name);
            $p.append($a);
            if(i == count - 1) {
               $('#gradesList').trigger('create');
               $.mobile.hidePageLoadingMsg();
            }
      });
      
   };
   
   
})(app.model.gradesList);

app.controller.gradesList = {};

(function(jQuery, mgradesList, vgradesList) {

   app.controller.gradesList.init = function() {

      // render : new function() {
      //mgcList.update(vgradesList.render);      
      $.ajax({
            type : 'GET',
            url : "/grade/controller",
            data : {
            op : 'listgrades',
            courseId : app.model.courseDetails.getId(),
            page : 'Grades.html'
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
            mgradesList.update(data);
            vgradesList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      //$('#gcList').die().live('pagechange', vgcList.render);
     
      //};
   };
   
  
})(jQuery, app.model.gradesList, app.view.gradesList);


/*** Grades Edit ***/
app.model.gradesEdit = {};

(function() {
   
   var name;
   
   var gcId;
  
   var gradesEdit;
   
   var studentList;
   
   app.model.gradesEdit.getName = function() {
      return name;
   };

   app.model.gradesEdit.setName = function(data) {
      name = data;
   };

   app.model.gradesEdit.setId = function(data) {
      gcId = data;
   };
   
   app.model.gradesEdit.getId = function() {
      return gcId;
   };
  
   app.model.gradesEdit.getStudentList = function() {
      return studentList;
   };
      
   app.model.gradesEdit.setStudentList = function(data) {
      studentList = data;
   };
   
   app.model.gradesEdit.setGCEdit = function(data) {
      gcEdit = data;
   };

   app.model.gradesEdit.getGCEdit = function() {
      return gcEdit;
   };
})();

app.view.gradesEdit = {};

(function(mgradesStudentList) {

   var gsu;

   app.view.gradesEdit.render = function() {

      //render : new function() {
      if(gsu === null && mgradesStudentList.get() === null)
         return;
           
      if (gsu === mgradesStudentList.get())         
         return;
      
      gsu = gradeStudentList.get();
      $("#gcEditStuList > p").remove();
      var count = gsu.gsu.length;
      $.mobile.hidePageLoadingMsg();
      $.mobile.showPageLoadingMsg("a", "Loading GradableComponent List....");
      $.each(gsu.gsu, function(i, gc) {
            var $p = $('<p></p>');
            var query = "GradeStudentEdit.html"; //?gcName=" + gc;
            var onclick = "app.model.gradeEdit.student.setName('" + gsu.name + "');app.model.gradeEdit.student.setPoints('" + gsu.points + "');app.model.gradeEdit.student.setGCId('" + gsu.id + "');app.model.gradeEdit.student.setStudentId('" + gsu.stuId + "');app.view.transfer('" + query + "')";
            var $a = $('<a data-role="button" style="text-align:left" data-transition="slide" onclick="' + onclick + '" href="' + query + '"></a>');            
            $('#gcEditStuList').append($p);
            $a.html(gsu.name);
            $p.append($a);
            if(i == count - 1) {
               $('#gcEditStuList').trigger('create');
               $.mobile.hidePageLoadingMsg();
            }
      });
      
   };
   
   
})(app.model.gcList);

app.controller.gradesEdit = {};

app.controller.gradesEdit.studentList = {};

(function(jQuery, mgradesEdit, vgradesEdit) {

   app.controller.gradesEdit.studentList.init = function() {

      // render : new function() {
      //mgcList.update(vgradesList.render);      
      $.ajax({
            type : 'GET',
            url : "/grade/controller",
            data : {
               op : 'listgradestudents',
               courseId : app.model.courseDetails.getId(),
               gcId : app.model.gradesEdit.getId(),
               page : 'Grades.html'
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
            mgradesList.setStudentList(data);
            vgradesList.render();            
         }
      }).fail(function(jqXHR, textStatus) {
            console.log(textStatus);
            $.mobile.hidePageLoadingMsg();
      });
     
      //$('#gcList').die().live('pagechange', vgcList.render);
     
      //};
   };
   
  
})(jQuery, app.model.gradesEdit, app.view.gradesList);

/***

/*** Grades Edit Student ***/
app.controller.gradesEdit.student = {};
app.model.gradesEdit.student = {};

(function() {
   
   var name;
   
   var gcId;
   
   var stuId;
  
   var points;
   
   app.model.gradesEdit.student.getName = function() {
      return name;
   };

   app.model.gradesEdit.student.setName = function(data) {
      name = data;
   };

   app.model.gradesEdit.student.setGCId = function(data) {
      gcId = data;
   };
   
   app.model.gradesEdit.student.getGCId = function() {
      return gcId;
   };
  
   app.model.gradesEdit.student.getStuId = function() {
      return stuId;
   };
      
   app.model.gradesEdit.student.setStuId = function(data) {
      stuId = data;
   };
   
   app.model.gradesEdit.student.setPoints = function(data) {
      points = data;
   };

   app.model.gradesEdit.student.getPoints = function() {
      return points;
   };
})();