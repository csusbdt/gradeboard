package gradesys;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONObject;

@SuppressWarnings("serial")
public class InstructorControllerServlet extends HttpServlet {	

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
			}			
		} catch (CourseAlreadyExistsException e) {
			return "{ \"err\": \"Course name already taken.\" }";
		}
		return "{}";	
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
			
			JSONObject jsonArray = new JSONObject();
			for(int i = 0; i < courses.size(); i++) {
				Course course = courses.get(i);
				jsonArray.put("courseName", course.getName());
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
