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