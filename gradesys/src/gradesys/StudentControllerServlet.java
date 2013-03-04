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
	
	static String testJson = "{\"title\":\"CSE 405 Grade Status Report\",\"students\":[{\"id\":\"dv\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"xz\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"sj\",\"total\":170,\"rank\":3,\"percent\":106,\"scores\":[10,10,20,10,10,20,30,10,30,20,\"\",\"\"],\"grade\":\"A\",\"attempted\":160},{\"id\":\"wq\",\"total\":120,\"rank\":4,\"percent\":109,\"scores\":[10,10,20,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"we\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zb\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"kt\",\"total\":108,\"rank\":7,\"percent\":98,\"scores\":[10,10,8,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"88\",\"total\":107,\"rank\":8,\"percent\":97,\"scores\":[10,10,7,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zm\",\"total\":106,\"rank\":9,\"percent\":96,\"scores\":[10,10,7,9,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"gh\",\"total\":100,\"rank\":10,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"zx\",\"total\":100,\"rank\":10,\"percent\":91,\"scores\":[10,10,0,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A-\",\"attempted\":110},{\"id\":\"df\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"ak\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"dh\",\"total\":97,\"rank\":14,\"percent\":97,\"scores\":[10,10,9,10,10,20,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"vb\",\"total\":95,\"rank\":15,\"percent\":95,\"scores\":[10,10,10,9,9,19,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"qw\",\"total\":81,\"rank\":16,\"percent\":74,\"scores\":[10,10,12,9,10,20,0,10,\"\",\"\",\"\",\"\"],\"grade\":\"C\",\"attempted\":110},{\"id\":\"dg\",\"total\":70,\"rank\":17,\"percent\":70,\"scores\":[10,10,10,10,10,20,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"C-\",\"attempted\":100},{\"id\":\"cc\",\"total\":65,\"rank\":18,\"percent\":65,\"scores\":[10,8,0,9,10,0,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"D\",\"attempted\":100},{\"id\":\"zo\",\"total\":45,\"rank\":19,\"percent\":45,\"scores\":[10,8,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rt\",\"total\":44,\"rank\":20,\"percent\":44,\"scores\":[10,7,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rx\",\"total\":30,\"rank\":21,\"percent\":38,\"scores\":[10,\"\",\"\",10,10,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":80},{\"id\":\"xc\",\"total\":17,\"rank\":22,\"percent\":17,\"scores\":[9,8,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"tj\",\"total\":14,\"rank\":23,\"percent\":14,\"scores\":[10,4,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"kl\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"ds\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"op\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"hj\",\"total\":8,\"rank\":27,\"percent\":8,\"scores\":[8,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"45\",\"total\":5,\"rank\":28,\"percent\":5,\"scores\":[5,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"yu\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"vm\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100}],\"objectives\":[\"java\",\"http\",\"thread\",\"gae\",\"bstrap\",\"ajax\",\"ds\",\"style\",\"number\",\"local\",\"secure\",\"apis\"],\"points\":[10,10,10,10,10,20,30,10,30,20,20,20]}";	

	private String listStudents(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String courseId = req.getParameter("courseId");
			if(courseId == null)
				return "{}";
			
			List<Student> students = Student.getStudentsByCourseId(courseId);
			
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
		try {
			if(studentnname == null || courseId == null || studentemail == null)
				return Util.getJsonErrorMsg("Missing paramter either instructorName or courseId or email.");

			Student.create(studentnname, courseId, studentemail);
			
			List<Student> students = Student.getStudentsByCourseId(courseId);
			
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
		try {
			if(studentnname == null || studentid == null || studentemail == null)
				return Util.getJsonErrorMsg("Missing paramter either studentname or email.");

			Student.update(studentid, studentnname, studentemail);
			
			List<Student> students = Student.getStudentsByCourseId(courseId);
			
			if(students.size() == 0)
				return "{}";
			
			return Util.getStudentsJson(students);
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
			
			return;
		}
		// Validate courseName and jsonData.
		String courseId = req.getParameter("courseId");
		if (courseId == null) {
			resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
			return;
		}
		String jsonData;
		try {
			if (false) throw new CourseNotFoundException();
			jsonData = testJson;
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