package gradesys;

import java.io.IOException;
import java.util.List;
<<<<<<< HEAD
import java.util.logging.Level;
import java.util.logging.Logger;
=======
>>>>>>> b8fdd260c2af41e4792b7942344ba051b1f0a7e9

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

<<<<<<< HEAD
import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
=======
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;
>>>>>>> b8fdd260c2af41e4792b7942344ba051b1f0a7e9

@SuppressWarnings("serial")
public class InstructorControllerServlet extends HttpServlet {
	
	 private static final Logger logger = Logger.getLogger(InstructorControllerServlet.class.getName());

	private String courseAdd(HttpServletRequest req, HttpServletResponse resp) {
		String courseName = req.getParameter("name");
		try {
			Course course = Course.create(courseName);
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByName(user.getUserId());
			if(instructor == null) {
				instructor = Instructor.create(user.getUserId());
				Auth.create(course.getID(), instructor.getKey());		
			} else {
				Auth.create(course.getID(), instructor.getKey());	
<<<<<<< HEAD
			}

			List<Course> courses = Auth.getCoursesByInstructor(instructor.getKey());
			return Util.getCoursesJson(courses);
=======
			}			
>>>>>>> b8fdd260c2af41e4792b7942344ba051b1f0a7e9
		} catch (CourseAlreadyExistsException e) {
			return "{ \"error\": \"Course name already taken.\" }";
		} catch (EntityNotFoundException e) {
			logger.log(Level.SEVERE, "Error getting courses", e);
			return "{}";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding course", e);
			return "{}";
		}	
	}
	
	private String listCourses(HttpServletRequest req, HttpServletResponse resp) {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByName(user.getUserId());
			if(instructor == null) {
				return "{}";
			}
			List<Course> courses = Auth.getCoursesByInstructor(instructor.getKey());
			return Util.getCoursesJson(courses);
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list courses.\" }";
		}	
	}
	
	private String listCourses(HttpServletRequest req, HttpServletResponse resp) {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByName(user.getUserId());
			if(instructor == null) {
				return "{}";
			}
			List<Course> courses = Auth.getCoursesByInstructor(instructor.getKey());
			if(courses.size() == 0)
				return "{}";
			
			JSONArray jsonArray = new JSONArray();
			for(int i = 0; i < courses.size(); i++) {
				Course course = courses.get(i);
				jsonArray.put(course.getName());
			}
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("courses", jsonArray);
			return jsonObject.toString();
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list courses.\" }";
		}	
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String operation = req.getParameter("op");
		if(operation != null) { // !Util.isEmpty(operation)) {
			if(operation.equalsIgnoreCase("courseadd")) {
				resp.getWriter().write(courseAdd(req, resp));
			}
		} else {
			resp.getWriter().print("{ \"err\": \"unknown operation\" }");
		}
	}
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String operation = req.getParameter("op");
		if(operation != null) { // !Util.isEmpty(operation)) {
			if(operation.equalsIgnoreCase("listCourses")) {
				resp.getWriter().write(listCourses(req, resp));
			}
		} else {
			resp.getWriter().print("{ \"err\": \"unknown operation\" }");
		}
	}
	
}
