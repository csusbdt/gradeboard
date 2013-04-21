package gradesys;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GradeControllerServlet extends HttpServlet {

	private static final Logger logger = Logger.getLogger(GradeControllerServlet.class.getName());

	private String listGradableComponents(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String courseId = req.getParameter("courseId");
			if(courseId == null)
				return "{}";
			
			List<GradableComponent> gcs = GradableComponent.getGradableComponentsByCourseId(Long.valueOf(courseId));
			
			if(gcs.size() == 0)
				return "{}";
			
			return Util.getGradableComponentJson(gcs);
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list gradable components.\" }";
		}	
	}
	
	private String listGrades(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String courseId = req.getParameter("courseId");
			if(courseId == null)
				return "{}";
			
			List<GradableComponent> gcs = GradableComponent.getGradableComponentsByCourseId(Long.valueOf(courseId));
			
			logger.info("Number of students in the gradable components : " + gcs.size());
			if(gcs.size() == 0)
				return "{}";
			
			return Util.getGradesJson(gcs);
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list gradable components.\" }";
		}	
	}
	
	
	private String listGradeStudents(HttpServletRequest req, HttpServletResponse resp) {
		try {
			String gcId = req.getParameter("gcId");
			String courseId = req.getParameter("courseId");
			if(courseId == null || gcId == null)
				return "{}";
			
			Map<Student, Grade> mapgs = Grade.getStudents(Long.valueOf(gcId), Long.valueOf(courseId));
			
			if(mapgs == null || mapgs.size() == 0)
				return "{}";
			
			return Util.getGradesStudentJson(mapgs, gcId, courseId);
		} catch (Exception e) {
			return "{ \"err\": \"Unable to list student components.\" }";
		}	
	}
	
	
	private String addGradableComponent(HttpServletRequest req, HttpServletResponse resp) {
		String gcname = req.getParameter("name");
		String gcpoints = req.getParameter("points");
		String gcdeadline = req.getParameter("deadline");
		String courseId = req.getParameter("courseId");
		try {
			if(gcname == null || courseId == null || gcpoints == null || gcdeadline == null)
				return Util.getJsonErrorMsg("Missing parameter either gradable component name or points or deadline.");

			GradableComponent.create(gcname, courseId, gcpoints, gcdeadline);
			
			List<GradableComponent> gcs = GradableComponent.getGradableComponentsByCourseId(Long.valueOf(courseId));
			
			if(gcs.size() == 0)
				return "{}";
			
			return Util.getGradableComponentJson(gcs);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error adding gradable component", e);
			return "{}";
		}	
	}
	
	private String editGradableComponent(HttpServletRequest req, HttpServletResponse resp) {
		String gcname = req.getParameter("name");
		String gcpoints = req.getParameter("points");
		String gcdeadline = req.getParameter("deadline");
		String courseId = req.getParameter("courseId");
		String gcId = req.getParameter("gcId");
		try {
			if(gcname == null || courseId == null || gcpoints == null || gcdeadline == null)
				return Util.getJsonErrorMsg("Missing parameter either gradable component name or points or deadline.");

			GradableComponent.update(gcId, courseId, gcname, gcpoints, gcdeadline);
			
			List<GradableComponent> gcs = GradableComponent.getGradableComponentsByCourseId(Long.valueOf(courseId));
			
			if(gcs.size() == 0)
				return "{}";
			
			return Util.getGradableComponentJson(gcs);
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error editing gradable component", e);
			return "{}";
		}	
	}
	
	private String saveStudentGrade(HttpServletRequest req, HttpServletResponse resp) {
		String stuid = req.getParameter("studentid");
		String gcId = req.getParameter("gcid");
		String pts = req.getParameter("points");
		try {
			if(stuid == null || gcId == null || pts == null || gcId == null)
				return Util.getJsonErrorMsg("Missing parameters.");

			Grade grade = Grade.saveGrade(Long.valueOf(gcId), Long.valueOf(stuid), Long.valueOf(pts), true);
			Long points = grade.getPoints();
			
			
			return "{}";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error editing gradable component", e);
			return "{}";
		}	
	}
	
	private String deletegc(HttpServletRequest req, HttpServletResponse resp) {
		String cid = req.getParameter("courseId");
		String gcId = req.getParameter("gcId");
		try {
			if(gcId == null || cid == null)
				return Util.getJsonErrorMsg("Missing parameters.");

			GradableComponent.deleteEntityByName(Long.valueOf(cid), Long.valueOf(gcId));
			
			return "{}";
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Error editing gradable component", e);
			return "{}";
		}	
	}
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		String operation = req.getParameter("op");
		if(operation != null) { // !Util.isEmpty(operation)) {
			if(operation.equalsIgnoreCase("addgc")) {
				resp.getWriter().write(addGradableComponent(req, resp));				
			}
			else if(operation.equalsIgnoreCase("editgc")) {
				resp.getWriter().write(editGradableComponent(req, resp));
			}
			else if(operation.equalsIgnoreCase("savestudentgrade")) {
				resp.getWriter().write(saveStudentGrade(req, resp));
			}
			else if(operation.equalsIgnoreCase("deletegc")) {
				resp.getWriter().write(deletegc(req, resp));
			}
			return;
		}
	}
	

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String operation = req.getParameter("op");
		if(operation != null) { // !Util.isEmpty(operation)) {
			if(operation.equalsIgnoreCase("listgcs")) {
				resp.getWriter().write(listGradableComponents(req, resp));
			}

			else if(operation.equalsIgnoreCase("listgrades")) {
				resp.getWriter().write(listGrades(req, resp));
			}
			
			else if(operation.equalsIgnoreCase("listgradestudents")) {
				resp.getWriter().write(listGradeStudents(req, resp));
			}
		} 		
		else {
			resp.getWriter().print("{ \"err\": \"unknown operation\" }");
		}
	}
	
}
