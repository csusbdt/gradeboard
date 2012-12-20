package gradesys;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class InstructorControllerServlet extends HttpServlet {	

	private String courseAdd(HttpServletRequest req, HttpServletResponse resp) {
		String courseName = req.getParameter("courseName");
		try {
			Course.create(courseName);
		} catch (CourseAlreadyExistsException e) {
			return "{ \"err\": \"Course name already taken.\" }";
		}
		return "{}";	
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
	
}
