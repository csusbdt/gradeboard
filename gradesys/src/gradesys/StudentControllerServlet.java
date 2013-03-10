package gradesys;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;

@SuppressWarnings("serial")
public class StudentControllerServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(StudentControllerServlet.class.getName());

	private String listStudents(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String courseId = req.getParameter("courseId");
			if(courseId == null)
				return "{}";
			
			Long cid;
			try {
				cid = Long.parseLong(courseId);
			} catch (NumberFormatException e) {
				return "{}";
			}
			
			List<Student> students = Student.getStudentsByCourseId(cid);
			
			if(students.size() == 0)
				return "{}";
			
			return Util.getStudentsJson(students);
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list courses.\" }";
		}	
	}	
	
	private String addStudent(HttpServletRequest req, HttpServletResponse resp) {
		String studentnname = req.getParameter("name");
		String studentemail = req.getParameter("email");
		String courseId = req.getParameter("courseId");
				
		Long cid;
		try {
			cid = Long.parseLong(courseId);
		} catch (NumberFormatException e) {
			return "{}";
		}

		try {
			if(studentnname == null || courseId == null || studentemail == null)
				return Util.getJsonErrorMsg("Missing paramter either instructorName or courseId or email.");

			Student.create(studentnname, courseId, studentemail);
			
			List<Student> students = Student.getStudentsByCourseId(cid);
			
			if(students.size() == 0)
				return "{}";
			
			return Util.getStudentsJson(students);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding instructor", e);
			return "{}";
		}	
	}
	
	private String editStudent(HttpServletRequest req, HttpServletResponse resp) {
		String studentnname = req.getParameter("name");
		String studentemail = req.getParameter("email");
		String studentid = req.getParameter("id");
		String courseId = req.getParameter("courseId");
		
		Long cid;
		try {
			cid = Long.parseLong(courseId);
		} catch (NumberFormatException e) {
			return "{}";
		}

		try {
			if(studentnname == null || studentid == null || studentemail == null)
				return Util.getJsonErrorMsg("Missing paramter either studentname or email.");

			Student.update(studentid, studentnname, studentemail);
			
			List<Student> students = Student.getStudentsByCourseId(cid);
			
			if(students.size() == 0)
				return "{}";
			
			return Util.getStudentsJson(students);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding instructor", e);
			return "{}";
		}	
	}
	
	private String deleteStudent(HttpServletRequest req, HttpServletResponse resp) {
		String studentid = req.getParameter("id");
		String courseId = req.getParameter("courseId");
		
		Long cid;
		try {
			cid = Long.parseLong(courseId);
		} catch (NumberFormatException e) {
			return "{}";
		}

		try {
			if(studentid == null || courseId == null)
				return Util.getJsonErrorMsg("Missing paramter either student id or courseId.");

			Student.delete(Long.valueOf(studentid), cid);
			Grade.deleteGradesByStudentId(Long.valueOf(studentid));
			return "{}";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding instructor", e);
			return "{}";
		}	
	}
	
	
	private String addStudentBulk(HttpServletRequest req,
			HttpServletResponse resp) {
		// TODO Auto-generated method stub
		String studentData = req.getParameter("studentdata");
		String courseId = req.getParameter("courseId");
		try {
			if(Util.isEmpty(studentData) || Util.isEmpty(courseId))
				return Util.getJsonErrorMsg("Missing paramter studentdata.");

			List<List<String>> studentList = null;
			try {
				studentList = Util.parseCSVformat(studentData);
			}catch(Exception ex) {
				return Util.getJsonErrorMsg(ex.getMessage());
			}
			
			try {
				return Student.createBulk(studentList, courseId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				return Util.getJsonErrorMsg(e.getMessage());
			}
//			
//			List<Student> students = Student.getStudentsByCourseId(courseId);
//			
//			if(students.size() == 0)
//				return "{}";
//			
//			return Util.getStudentsJson(students);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding instructor", e);
			return "{}";
		}	
	}

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String operation = req.getParameter("op");
		if(operation != null) { // !Util.isEmpty(operation)) {
			if(operation.equalsIgnoreCase("addstudent")) {
				resp.getWriter().write(addStudent(req, resp));				
			}
			else if(operation.equalsIgnoreCase("editstudent")) {
				resp.getWriter().write(editStudent(req, resp));
			}
			
			else if(operation.equalsIgnoreCase("addstudentsbulk")) {
				resp.getWriter().write(addStudentBulk(req, resp));
			}
			
			else if(operation.equalsIgnoreCase("deletestudent")) {
				resp.getWriter().write(deleteStudent(req, resp));
			}
			
			return;
		}
		// Validate courseName and jsonData.
		String courseId = req.getParameter("courseId");
		if (courseId == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		long cid = 0;
		try {
			cid = Long.parseLong(courseId);
		} catch (NumberFormatException e) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String jsonData;
		try {
			jsonData = Course.getGradesheetJson(cid);
		} catch (CourseNotFoundException e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		resp.setContentType("application/json");
		resp.getWriter().print(jsonData);
	}
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String operation = req.getParameter("op");
		if(operation != null) { // !Util.isEmpty(operation)) {
			if(operation.equalsIgnoreCase("liststudent")) {
				resp.getWriter().write(listStudents(req, resp));
			}
			/*else if(operation.equalsIgnoreCase("listinstructor")) {				
				resp.getWriter().write(listInstructor(req, resp));
			}*/
		} 		
		else {
			resp.getWriter().print("{ \"err\": \"unknown operation\" }");
		}
	}
	
}