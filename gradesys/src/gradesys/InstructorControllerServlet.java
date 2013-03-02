package gradesys;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.datastore.EntityNotFoundException;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.appengine.labs.repackaged.org.json.JSONArray;
import com.google.appengine.labs.repackaged.org.json.JSONObject;


@SuppressWarnings("serial")
public class InstructorControllerServlet extends HttpServlet {
	
	 private static final Logger logger = Logger.getLogger(InstructorControllerServlet.class.getName());

	private String addCourse(HttpServletRequest req, HttpServletResponse resp) {
		String courseName = req.getParameter("name");
		try {
			Course course = Course.create(courseName);
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null) {
				instructor = Instructor.create(user.getNickname(), user.getUserId(), user.getEmail());
				Auth.create(course.getID(), instructor.getKey(), AuthPermissions.SUPER_USER);		
			} else {
				Auth.create(course.getID(), instructor.getKey(), AuthPermissions.SUPER_USER);
			}

			List<Course> courses = Auth.getCoursesByInstructor(instructor.getKey());
			return Util.getCoursesJson(courses);
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
	
	private String addInstructor(HttpServletRequest req, HttpServletResponse resp) {
		String instructorName = req.getParameter("instructorname");
		String instructorEmail = req.getParameter("instructoremail");
		String courseId = req.getParameter("courseId");
		try {
			if(instructorName == null || courseId == null || instructorEmail == null)
				return Util.getMissingParameter("Missing paramter either instructorName or courseId or email.");

			Course course = Course.create(courseId);
			if(course == null) {
				return Util.courseNotFoundError();
			}
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null) {
				instructor = Instructor.getByInstructorEmail(instructorEmail);
				if(instructor == null) {
					Instructor.create(instructorName, "", instructorEmail);
				}
			} 
			Auth.create(course.getID(), instructor.getKey(), AuthPermissions.USER);
			List<Course> courses = Auth.getCoursesByInstructor(instructor.getKey());
			return Util.getCoursesJson(courses);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding instructor", e);
			return "{}";
		}	
	}	
	
	private String listInstructor(HttpServletRequest req, HttpServletResponse resp) {
		String courseId = req.getParameter("courseId");
		try {
			if(courseId == null)
				return Util.getMissingParameter("Missing paramter courseId.");

			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null)
				return Util.getMissingParameter("Unable to find instructor with emailId  " + user.getEmail());

			List<Instructor> instructors = Instructor.getInstructorsByCourseId(courseId, instructor.getKey());
			return Util.getInstructorsJson(instructors);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error geting list of instructors", e);
			return "{}";
		}	
	}
	
	private String saveCourse(HttpServletRequest req, HttpServletResponse resp) {
		String courseId = req.getParameter("courseId");
		String newCourseName = req.getParameter("courseName");
		if(Util.isEmpty(courseId) || Util.isEmpty(newCourseName)) {
			return "{}";
		}
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null) {
				return "{}";		
			}
			Course course = Course.save(courseId, newCourseName);
			return Util.getCourseJson(course.getName(), courseId);			
		} catch (CourseNotFoundException e) {
			logger.log(Level.SEVERE, "Error getting courses", e);
			return "{ \"error\": \"Course that you are saving does not exists.\" }";
		} catch (EntityNotFoundException e) {
			logger.log(Level.SEVERE, "Error getting courses", e);
			return "{ \"error\": \"Course that you are saving does not exists.\" }";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error saving course", e);
			return "{}";
		}	
	}
	
	private String listCourses(HttpServletRequest req, HttpServletResponse resp) {
		try {
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null) {
				instructor = Instructor.getByInstructorEmail(user.getEmail());
				if(instructor == null) {
					return "{}";
				}
			}
			Instructor.updateUserId(user.getUserId(), user.getNickname(), user.getEmail());
			List<Course> courses = Auth.getCoursesByInstructor(instructor.getKey());
			if(courses.size() == 0)
				return "{}";
			
			return Util.getCoursesJson(courses);
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list courses.\" }";
		}	
	}
	
	private String listCourse(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String courseId = req.getParameter("id");
			if(Util.isEmpty(courseId)) {
				return "{}";
			}
			UserService userService = UserServiceFactory.getUserService();
			User user = userService.getCurrentUser();
			Instructor instructor = Instructor.getByUserId(user.getUserId());
			if(instructor == null) {
				return "{}";
			}
			Course course = Auth.getCourseDetailsByInstructor(courseId, instructor.getKey());
			if(course == null)
				return "{}";
			
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("courseName", course.getName());
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
				resp.getWriter().write(addCourse(req, resp));
			}
			if(operation.equalsIgnoreCase("coursenamesave")) {				
				resp.getWriter().write(saveCourse(req, resp));
			}
			if(operation.equalsIgnoreCase("listcourse")) {				
				resp.getWriter().write(listCourse(req, resp));
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
			else if(operation.equalsIgnoreCase("listinstructor")) {				
				resp.getWriter().write(listInstructor(req, resp));
			}
		} 
		
		else {
			resp.getWriter().print("{ \"err\": \"unknown operation\" }");
		}
	}
	
}
