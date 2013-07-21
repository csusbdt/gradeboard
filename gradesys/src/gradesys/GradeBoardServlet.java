package gradesys;

import java.io.IOException;
//import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class GradeBoardServlet extends HttpServlet {

//	private static final Logger logger = Logger.getLogger(GradeBoardServlet.class.getName());

	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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
			jsonData = Util.getGradesheetJson(cid);
		} catch (Exception e) {
			resp.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}
		resp.setContentType("application/json");
		resp.getWriter().print(jsonData);
	}
	
}