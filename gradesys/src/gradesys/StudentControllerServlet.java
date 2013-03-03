package gradesys;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class StudentControllerServlet extends HttpServlet {

	static String testJson = "{\"title\":\"CSE 405 Grade Status Report\",\"students\":[{\"id\":\"dv\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"xz\",\"total\":210,\"rank\":1,\"percent\":105,\"scores\":[10,10,20,10,10,20,30,10,30,20,20,20],\"grade\":\"A\",\"attempted\":200},{\"id\":\"sj\",\"total\":170,\"rank\":3,\"percent\":106,\"scores\":[10,10,20,10,10,20,30,10,30,20,\"\",\"\"],\"grade\":\"A\",\"attempted\":160},{\"id\":\"wq\",\"total\":120,\"rank\":4,\"percent\":109,\"scores\":[10,10,20,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"we\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zb\",\"total\":110,\"rank\":5,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"kt\",\"total\":108,\"rank\":7,\"percent\":98,\"scores\":[10,10,8,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"88\",\"total\":107,\"rank\":8,\"percent\":97,\"scores\":[10,10,7,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"zm\",\"total\":106,\"rank\":9,\"percent\":96,\"scores\":[10,10,7,9,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":110},{\"id\":\"gh\",\"total\":100,\"rank\":10,\"percent\":100,\"scores\":[10,10,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"zx\",\"total\":100,\"rank\":10,\"percent\":91,\"scores\":[10,10,0,10,10,20,30,10,\"\",\"\",\"\",\"\"],\"grade\":\"A-\",\"attempted\":110},{\"id\":\"df\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"ak\",\"total\":98,\"rank\":12,\"percent\":98,\"scores\":[10,8,10,10,10,20,30,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"dh\",\"total\":97,\"rank\":14,\"percent\":97,\"scores\":[10,10,9,10,10,20,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"vb\",\"total\":95,\"rank\":15,\"percent\":95,\"scores\":[10,10,10,9,9,19,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"A\",\"attempted\":100},{\"id\":\"qw\",\"total\":81,\"rank\":16,\"percent\":74,\"scores\":[10,10,12,9,10,20,0,10,\"\",\"\",\"\",\"\"],\"grade\":\"C\",\"attempted\":110},{\"id\":\"dg\",\"total\":70,\"rank\":17,\"percent\":70,\"scores\":[10,10,10,10,10,20,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"C-\",\"attempted\":100},{\"id\":\"cc\",\"total\":65,\"rank\":18,\"percent\":65,\"scores\":[10,8,0,9,10,0,28,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"D\",\"attempted\":100},{\"id\":\"zo\",\"total\":45,\"rank\":19,\"percent\":45,\"scores\":[10,8,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rt\",\"total\":44,\"rank\":20,\"percent\":44,\"scores\":[10,7,9,9,9,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"rx\",\"total\":30,\"rank\":21,\"percent\":38,\"scores\":[10,\"\",\"\",10,10,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":80},{\"id\":\"xc\",\"total\":17,\"rank\":22,\"percent\":17,\"scores\":[9,8,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"tj\",\"total\":14,\"rank\":23,\"percent\":14,\"scores\":[10,4,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"kl\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"ds\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"op\",\"total\":10,\"rank\":24,\"percent\":10,\"scores\":[10,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"hj\",\"total\":8,\"rank\":27,\"percent\":8,\"scores\":[8,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"45\",\"total\":5,\"rank\":28,\"percent\":5,\"scores\":[5,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"yu\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100},{\"id\":\"vm\",\"total\":0,\"rank\":29,\"percent\":0,\"scores\":[0,0,0,0,0,0,0,\"\",\"\",\"\",\"\",\"\"],\"grade\":\"F\",\"attempted\":100}],\"objectives\":[\"java\",\"http\",\"thread\",\"gae\",\"bstrap\",\"ajax\",\"ds\",\"style\",\"number\",\"local\",\"secure\",\"apis\"],\"points\":[10,10,10,10,10,20,30,10,30,20,20,20]}";	
	
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
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

}
